package com.gzc.domain.trade.service.lock;


import com.gzc.domain.tag.adapter.repository.ITagRepository;
import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.UserEntity;
import com.gzc.domain.trade.model.entity.resp.MarketPayOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeOrderService implements ITradeOrderService {

    private final ITradeRepository tradeRepository;

    @Override
    public MarketPayOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo) {

        log.info("拼团交易-查询未完成营销订单:{} outTradeNo:{}", userId, outTradeNo);
        return tradeRepository.queryUnfinishedPayOrderByOutTradeNo(userId, outTradeNo);
    }


    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        log.info("拼团交易-查询拼单进度:{}", teamId);
        return tradeRepository.queryGroupBuyProgress(teamId);
    }


    @Override
    public MarketPayOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {

        // 锁定聚合订单 - 这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
        return tradeRepository.lockMarketPayOrder(userId, payActivityEntity, payDiscountEntity);
    }
}
