package com.gzc.domain.trade.service.settlement;


import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSettlementService implements ITradeSettlementService {

    private final ITradeRepository tradeRepository;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) {

        // 1.查询组团进度信息
        String teamId = tradePaySuccessEntity.getTeamId();
        GroupBuyProgressVO groupBuyProgressVO = tradeRepository.querygroupBuyProgressVOByTeamId(teamId);

        // 2.拼团交易结算
        tradeRepository.settlementProcess(tradePaySuccessEntity, groupBuyProgressVO);

        return null;// todo
    }
}
