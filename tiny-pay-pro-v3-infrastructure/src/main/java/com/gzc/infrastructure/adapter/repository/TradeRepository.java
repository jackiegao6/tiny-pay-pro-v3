package com.gzc.infrastructure.adapter.repository;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;
import com.gzc.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import com.gzc.infrastructure.dao.IGroupBuyOrderDao;
import com.gzc.infrastructure.dao.IGroupBuyOrderListDao;
import com.gzc.infrastructure.dao.INotifyTaskDao;
import com.gzc.infrastructure.dao.po.GroupBuyOrder;
import com.gzc.infrastructure.dao.po.GroupBuyOrderList;
import com.gzc.infrastructure.dao.po.NotifyTask;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TradeRepository implements ITradeRepository {

    private final IGroupBuyOrderListDao orderListDao;
    private final IGroupBuyOrderDao orderDao;
    private final INotifyTaskDao notifyTaskDao;

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
                .originalPrice(groupBuyOrderListResp.getOriginalPrice())
                .deductionPrice(groupBuyOrderListResp.getOriginalPrice().subtract(groupBuyOrderListResp.getCurrentPrice()))
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
    public LockedOrderEntity lockOrderProcess(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {

        String teamId = payActivityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)){
            // 创团
            teamId = RandomStringUtils.randomNumeric(8);
            // 构建拼团订单
            ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
            LocalDateTime validStartTimeLDT = LocalDateTime.now();
            Date validStartTime = Date.from(validStartTimeLDT.atZone(shanghaiZone).toInstant());
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
                    .notifyUrl(payActivityEntity.getNotifyUrl())
                    .build();

            // 写入记录
            orderDao.insert(groupBuyOrder);
        }else{
            orderDao.updateAddLockCount(teamId);
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
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getOriginalPrice().subtract(payDiscountEntity.getCurrentPrice()))
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
                .validEndTime(groupBuyOrderResp.getValidEndTime())
                .notifyUrl(groupBuyOrderResp.getNotifyUrl())
                .build();
    }

    @Transactional
    @Override
    public void settlementProcess(TradePaySuccessEntity tradePaySuccessEntity, GroupBuyProgressVO groupBuyProgressVO) {

        String userId = tradePaySuccessEntity.getUserId();
        String outTradeNo = tradePaySuccessEntity.getOutTradeNo();
        String teamId = groupBuyProgressVO.getTeamId();

        // 1. 更新未支付订单状态为已支付
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);
        groupBuyOrderListReq.setOutTradeTime(groupBuyProgressVO.getOutTradeTime());
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

        // 支付完成写入回调任务记录
        Long activityId = groupBuyProgressVO.getActivityId();
        String notifyUrl = groupBuyProgressVO.getNotifyUrl();
        NotifyTask notifyTask = NotifyTask.builder()
                .activityId(activityId)
                .teamId(teamId)
                .notifyUrl(notifyUrl)
                .notifyCount(0)
                .notifyStatus(0)
                .parameterJson(
                        JSON.toJSONString(new HashMap<String, Object>() {{
                            put("teamId", teamId);
                            put("userId", userId);
                            put("outTradeNo", outTradeNo);
                        }})
                )
                .build();

        notifyTaskDao.insert(notifyTask);

        // 3. 更新拼团完成状态
        Integer targetCount = groupBuyProgressVO.getTargetCount();
        Integer completeCount = groupBuyProgressVO.getCompleteCount();
        if (targetCount - completeCount == 1) {
            int updateOrderStatusCount = orderDao.updateOrderStatus2COMPLETE(teamId);
            if (1 != updateOrderStatusCount) {
                log.error("更新组队状态为完结 失败");
                throw new AppException(ResponseCode.UPDATE_ORDER_STATUS_FAILED.getInfo());
            }
            // 如果是队列里的最后一个人 则 更新组队状态为完结态
            // 用mq通知
            // todo

        }

    }

    @Override
    public List<NotifyTaskVO> queryUnExecutedNotifyTaskList() {
        List<NotifyTask> notifyTasks = notifyTaskDao.queryUnExecutedNotifyTaskList();
        return BeanUtil.copyToList(notifyTasks, NotifyTaskVO.class);
    }

    /**
     * 根据teamId查找里面 还没有做结算回调处理的任务
     */
    @Override
    public List<NotifyTaskVO> queryUnExecutedNotifyTaskList(String teamId) {
        NotifyTask notifyTask = notifyTaskDao.queryUnExecutedNotifyTaskByTeamId(teamId);

        NotifyTaskVO notifyTaskVO = BeanUtil.copyProperties(notifyTask, NotifyTaskVO.class);
        return Collections.singletonList(notifyTaskVO);
    }

    @Override
    public int updateNotifyTaskStatusSuccess(String teamId) {
        return notifyTaskDao.updateNotifyTaskStatusSuccess(teamId);
    }

    @Override
    public int updateNotifyTaskStatusError(String teamId) {
        return notifyTaskDao.updateNotifyTaskStatusError(teamId);
    }

    @Override
    public int updateNotifyTaskStatusRetry(String teamId) {
        return notifyTaskDao.updateNotifyTaskStatusRetry(teamId);
    }

    @Override
    public String queryTeamIdByUserIdAndOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userId)
                .outTradeNo(outTradeNo)
                .build();
        return orderListDao.queryTeamIdByUserIdAndOutTradeNo(groupBuyOrderListReq);
    }
}
