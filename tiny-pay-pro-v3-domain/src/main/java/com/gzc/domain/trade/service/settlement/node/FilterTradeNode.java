package com.gzc.domain.trade.service.settlement.node;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.settlement.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterTradeNode extends AbstractNodeSupport {

    private final SettlementTradeNode settlementTradeNode;

    @Override
    public TradePaySettlementEntity apply(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {

        // 1.查询组团进度信息
        String teamId = reqParam.getTeamId();
        GroupBuyProgressVO groupBuyProgressVO = tradeRepository.querygroupBuyProgressVOByTeamId(teamId);

        // 外部交易时间 - 也就是用户支付完成的时间，这个时间要在拼团有效时间范围内
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        LocalDateTime outTradeTimeLDT = LocalDateTime.now();
        Date outTradeTime = Date.from(outTradeTimeLDT.atZone(shanghaiZone).toInstant());
        Date validEndTime = groupBuyProgressVO.getValidEndTime();
        // 判断，外部交易时间，要小于拼团结束时间。否则抛异常。
        if (!outTradeTime.before(validEndTime)) {
            log.error("订单交易时间不在拼团有效时间范围内");
            throw new AppException(ResponseCode.WRONG_TIME_FOR_OUT_TRADE.getInfo());
        }
        context.setGroupBuyProgressVO(groupBuyProgressVO);
        context.setOutTradeTime(outTradeTime);
        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TradePaySuccessEntity, DynamicContext, TradePaySettlementEntity> get(TradePaySuccessEntity reqParam, DynamicContext context) throws Exception {
        return settlementTradeNode;
    }
}
