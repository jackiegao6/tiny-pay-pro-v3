package com.gzc.domain.trade.service.settlement.node;


import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;
import com.gzc.domain.trade.service.notify.ITradeNotifyService;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementTradeNode extends AbstractNodeSupport {
    private final EndTradeNode endTradeNode;
    @Resource
    private ITradeNotifyService tradeNotifyService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public TradePaySettlementEntity apply(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {

        GroupBuyProgressVO groupBuyProgressVO = context.getGroupBuyProgressVO();
        groupBuyProgressVO.setOutTradeTime(context.getOutTradeTime());
        groupBuyProgressVO.setTeamId(context.getTeamId());


        NotifyTaskVO notifyTaskVO = tradeRepository.settlementProcess(reqParam, groupBuyProgressVO);
        // 到这有了某一个的回调任务

        // 进而进行 组队回调处理
        if (null != notifyTaskVO){
            // 引入异步线程的 方式去执行
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Integer> notifyResultMap = tradeNotifyService.execSettlementNotifyJob(notifyTaskVO);
                        log.info("回调通知拼团完结 result:{}", JSON.toJSONString(notifyResultMap));
                    } catch (Exception e) {
                        throw new AppException(e.getMessage());
                    }
                }
            });

        }

        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> get(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return endTradeNode;
    }
}
