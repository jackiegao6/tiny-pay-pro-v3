package com.gzc.domain.trade.adapter.repository;

import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.UserEntity;
import com.gzc.domain.trade.model.entity.resp.MarketPayOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeRepository {


    MarketPayOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo);

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    MarketPayOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity);

}
