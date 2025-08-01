package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import com.gzc.infrastructure.dao.IGroupBuyOrderDao;
import com.gzc.infrastructure.dao.IGroupBuyOrderListDao;
import com.gzc.infrastructure.dao.po.GroupBuyOrder;
import com.gzc.infrastructure.dao.po.GroupBuyOrderList;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TradeRepository implements ITradeRepository {

    private final IGroupBuyOrderListDao orderListDao;
    private final IGroupBuyOrderDao orderDao;

    @Override
    public LockedOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo) {

        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userId)
                .outTradeNo(outTradeNo)
                .build();
        GroupBuyOrderList groupBuyOrderListResp = orderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);
        if (groupBuyOrderListResp == null) return null;
        log.info("存在未完成的营销订单");
        return LockedOrderEntity.builder()
                .orderId(groupBuyOrderListResp.getOrderId())
                .currentPrice(groupBuyOrderListResp.getCurrentPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .teamId(groupBuyOrderListResp.getTeamId())
                .build();
    }


    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        GroupBuyOrder groupBuyOrderResp = orderDao.queryGroupBuyProgressVOByTeamId(teamId);

        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrderResp.getTargetCount())
                .completeCount(groupBuyOrderResp.getCompleteCount())
                .lockCount(groupBuyOrderResp.getLockCount())
                .build();
    }


    @Transactional(timeout = 500)
    @Override
    public LockedOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {

        String teamId = payActivityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)){
            // 创团
            teamId = RandomStringUtils.randomNumeric(8);
            // 构建拼团订单
            Date validStartTime = new Date();
            Integer validTime = payActivityEntity.getValidTime();
            Date validEndTime = new Date(validStartTime.getTime() + validTime * 60 * 1000L);

            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .currentPrice(payDiscountEntity.getCurrentPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    .validStartTime(validStartTime)
                    .validEndTime(validEndTime)
                    .build();

            // 写入记录
            orderDao.insert(groupBuyOrder);
        }else{
            // 更新记录 - 如果更新记录不等于1，则表示拼团已满，抛出异常 todo 感觉不需要 因为 在controller层里面拦截过了
            int updateCompletedNum  = orderDao.updateAddLockCount(teamId);
            if (updateCompletedNum != 1) throw new AppException(ResponseCode.FULL_TEAM.getInfo());
        }

        String orderId = RandomStringUtils.randomNumeric(12);
        String bizId = payActivityEntity.getActivityId() + "-" + userId;

        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userId)
                .teamId(teamId)
                .orderId(orderId)
                .activityId(payActivityEntity.getActivityId())
                .startTime(payActivityEntity.getStartTime())
                .endTime(payActivityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .currentPrice(payDiscountEntity.getCurrentPrice())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                .bizId(bizId)
                .build();
        orderListDao.insert(groupBuyOrderListReq);

        return LockedOrderEntity.builder()
                .orderId(orderId)
                .currentPrice(payDiscountEntity.getCurrentPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .teamId(teamId)
                .build();

    }

    @Override
    public GroupBuyProgressVO querygroupBuyProgressVOByTeamId(String teamId) {
        GroupBuyOrder groupBuyOrderResp = orderDao.queryGroupBuyProgressVOByTeamId(teamId);
        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrderResp.getTargetCount())
                .completeCount(groupBuyOrderResp.getCompleteCount())
                .lockCount(groupBuyOrderResp.getLockCount())
                .teamId(groupBuyOrderResp.getTeamId())
                .activityId(groupBuyOrderResp.getActivityId())
                .status(TradeOrderStatusEnumVO.valueOf(groupBuyOrderResp.getStatus()))
                .build();
    }

    @Transactional
    @Override
    public void settlementProcess(TradePaySuccessEntity tradePaySuccessEntity, GroupBuyProgressVO groupBuyProgressVO) {

        String userId = tradePaySuccessEntity.getUserId();
        String outTradeNo = tradePaySuccessEntity.getOutTradeNo();
        String teamId = tradePaySuccessEntity.getTeamId();

        // 1. 更新未支付订单状态为已支付
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);
        int updateOrderListStatusCount = orderListDao.updateOrderListStatus2COMPLETE(groupBuyOrderListReq);
        if (1 != updateOrderListStatusCount) {
            log.error("更新未支付订单状态至已支付失败");
            throw new AppException(ResponseCode.UPDATE_ORDER_LIST_STATUS_FAILED.getInfo());
        }

        // 2. 更新拼团达成数量
        int updateOrderCompletedCount = orderDao.updateOrderCompletedCount(teamId);
        if (1 != updateOrderCompletedCount) {
            log.error("更新拼团达成数量失败");
            throw new AppException(ResponseCode.UPDATE_COMPLETED_ORDER_STATUS_FAILED.getInfo());
        }

        // 3. 更新拼团完成状态
        Integer targetCount = groupBuyProgressVO.getTargetCount();
        Integer completeCount = groupBuyProgressVO.getCompleteCount();
        if (targetCount - completeCount == 1) {
            int updateOrderStatusCount = orderDao.updateOrderStatus2COMPLETE(teamId);
            if (1 != updateOrderStatusCount) {
                log.error("更新组队状态至成功失败");
                throw new AppException(ResponseCode.UPDATE_ORDER_STATUS_FAILED.getInfo());
            }

            // 查询拼团交易完成外部单号列表
            List<String> outTradeNoList = orderListDao.queryCompletedOutTradeNoListByTeamId(teamId);
            log.info(String.valueOf(outTradeNoList));
            // 拼团完成写入回调任务记录
//            NotifyTask notifyTask = new NotifyTask();
//            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());
//            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());
//            notifyTask.setNotifyUrl("暂无");
//            notifyTask.setNotifyCount(0);
//            notifyTask.setNotifyStatus(0);
//            notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>() {{
//                put("teamId", groupBuyTeamEntity.getTeamId());
//                put("outTradeNoList", outTradeNoList);
//            }}));
//
//            notifyTaskDao.insert(notifyTask);
        }


    }
}
