package com.gzc.domain.trade.adapter.repository;

import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;

import java.util.Date;

public interface ITradeRepository {


    LockedOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo);

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    LockedOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity);

    GroupBuyProgressVO querygroupBuyProgressVOByTeamId(String teamId);

    void settlementProcess(TradePaySuccessEntity tradePaySuccessEntity, GroupBuyProgressVO groupBuyProgressVO);

}
