package com.gzc.domain.trade.service.settlement.node;


import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementTradeNode extends AbstractNodeSupport {
    private final EndTradeNode endTradeNode;

    @Override
    public TradePaySettlementEntity apply(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {

        GroupBuyProgressVO groupBuyProgressVO = context.getGroupBuyProgressVO();
        // 2.拼团交易结算
        Date outTradeTime = context.getOutTradeTime();
        groupBuyProgressVO.setOutTradeTime(outTradeTime);
        tradeRepository.settlementProcess(reqParam, groupBuyProgressVO);

        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> get(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return endTradeNode;
    }
}
