package com.gzc.domain.trade.service.settlement.node;

import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterTradeNode extends AbstractNodeSupport {

    private final SettlementTradeNode settlementTradeNode;

    @Override
    public TradePaySettlementEntity apply(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> get(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return settlementTradeNode;
    }
}
