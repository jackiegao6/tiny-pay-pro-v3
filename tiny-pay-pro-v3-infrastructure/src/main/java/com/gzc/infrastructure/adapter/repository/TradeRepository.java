package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.UserEntity;
import com.gzc.domain.trade.model.entity.resp.MarketPayOrderEntity;
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


@Repository
@Slf4j
@RequiredArgsConstructor
public class TradeRepository implements ITradeRepository {


    private final IGroupBuyOrderListDao orderListDao;
    private final IGroupBuyOrderDao orderDao;

    @Override
    public MarketPayOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo) {

        GroupBuyOrderList groupBuyOrderListReq = GroupBuyOrderList.builder()
                .userId(userId)
                .outTradeNo(outTradeNo)
                .build();
        GroupBuyOrderList groupBuyOrderListResp = orderListDao.queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);
        if (groupBuyOrderListResp == null) return null;
        log.info("存在未完成的营销订单");
        return MarketPayOrderEntity.builder()
                .orderId(groupBuyOrderListResp.getOrderId())
                .currentPrice(groupBuyOrderListResp.getCurrentPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .teamId(groupBuyOrderListResp.getTeamId())
                .build();
    }


    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        GroupBuyOrder groupBuyOrderResp = orderDao.queryGroupBuyProgress(teamId);

        return GroupBuyProgressVO.builder()
                .targetCount(groupBuyOrderResp.getTargetCount())
                .completeCount(groupBuyOrderResp.getCompleteCount())
                .lockCount(groupBuyOrderResp.getLockCount())
                .build();
    }


    @Transactional(timeout = 500)
    @Override
    public MarketPayOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {

        String teamId = payActivityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)){
            // 创团
            teamId = RandomStringUtils.randomNumeric(8);
            // 构建拼团订单
            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .currentPrice(payDiscountEntity.getCurrentPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .completeCount(0)
                    .lockCount(1)
                    .build();

            // 写入记录
            orderDao.insert(groupBuyOrder);
        }else{
            // 更新记录 - 如果更新记录不等于1，则表示拼团已满，抛出异常 todo 感觉不需要 因为 在controller层里面拦截过了
            int updateCompletedNum  = orderDao.updateAddLockCount(teamId);
            if (updateCompletedNum != 1) throw new AppException(ResponseCode.FULL_TEAM.getInfo());
        }

        String orderId = RandomStringUtils.randomNumeric(12);
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
                .build();
        orderListDao.insert(groupBuyOrderListReq);

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .currentPrice(payDiscountEntity.getCurrentPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .teamId(teamId)
                .build();

    }
}
