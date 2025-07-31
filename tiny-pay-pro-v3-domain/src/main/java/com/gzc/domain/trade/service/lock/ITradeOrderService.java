package com.gzc.domain.trade.service.lock;

import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.UserEntity;
import com.gzc.domain.trade.model.entity.resp.MarketPayOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeOrderService {

    /**
     * 由用户id 和 锁单编号查询 查询未支付营销订单
     */
    MarketPayOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo);


    /**
     * 根据组队id 查询 拼团进度
     */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);


    /**
     * 锁定营销优惠支付订单
     */
    MarketPayOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity);
}
