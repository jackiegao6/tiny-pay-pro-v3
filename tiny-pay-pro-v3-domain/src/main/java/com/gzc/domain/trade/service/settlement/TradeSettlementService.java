package com.gzc.domain.trade.service.settlement;


import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.service.settlement.factory.DefaultTradeNodeFactory;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSettlementService implements ITradeSettlementService {

    private final DefaultTradeNodeFactory defaultTradeNodeFactory;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception {
        NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> rootNode = defaultTradeNodeFactory.getRootNode();
        return rootNode.apply(tradePaySuccessEntity, new DynamicContext());
    }
}
