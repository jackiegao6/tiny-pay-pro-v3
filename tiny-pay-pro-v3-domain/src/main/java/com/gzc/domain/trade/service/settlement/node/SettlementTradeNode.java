package com.gzc.domain.trade.service.settlement.node;


import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.notify.ITradeNotifyService;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementTradeNode extends AbstractNodeSupport {
    private final EndTradeNode endTradeNode;
    @Resource
    private ITradeNotifyService tradeNotifyService;

    @Override
    public TradePaySettlementEntity apply(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {

        GroupBuyProgressVO groupBuyProgressVO = context.getGroupBuyProgressVO();
        groupBuyProgressVO.setOutTradeTime(context.getOutTradeTime());
        groupBuyProgressVO.setTeamId(context.getTeamId());


        tradeRepository.settlementProcess(reqParam, groupBuyProgressVO);
        // 到这有了某一个的回调任务

        // 进而进行 组队回调处理
        Map<String, Integer> notifyResultMap = tradeNotifyService.execSettlementNotifyJob(context.getTeamId());
        log.info("回调通知拼团完结 result:{}", JSON.toJSONString(notifyResultMap));

        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> get(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return endTradeNode;
    }
}
