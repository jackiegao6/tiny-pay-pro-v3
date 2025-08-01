package com.gzc.domain.trade.service.settlement.node;

import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.AbstractNodeRouter;

import javax.annotation.Resource;

abstract class AbstractNodeSupport extends AbstractNodeRouter<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> {

    @Resource
    protected ITradeRepository tradeRepository;
}
