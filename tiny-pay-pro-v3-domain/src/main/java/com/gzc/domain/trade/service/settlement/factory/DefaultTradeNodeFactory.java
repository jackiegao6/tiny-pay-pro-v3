package com.gzc.domain.trade.service.settlement.factory;

import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.domain.trade.service.settlement.node.RootTradeNode;
import com.gzc.types.design.framework.tree.NodeHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultTradeNodeFactory {

    private final RootTradeNode rootTradeNode;

    public DefaultTradeNodeFactory(RootTradeNode rootTradeNode) {
        this.rootTradeNode = rootTradeNode;
    }

    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> getRootNode(){
        return rootTradeNode;
    }
}
