package com.gzc.domain.trade.service.lock;


import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeLockOrderService implements ITradeLockOrderService {

    private final ITradeRepository tradeRepository;

    @Override
    public LockedOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo) {

        return tradeRepository.queryUnfinishedPayOrderByOutTradeNo(userId, outTradeNo);
    }


    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {

        return tradeRepository.queryGroupBuyProgress(teamId);
    }


    @Override
    public LockedOrderEntity lockMarketPayOrder(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) {

        // 锁定聚合订单 - 这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
        return tradeRepository.lockOrderProcess(userId, payActivityEntity, payDiscountEntity);
    }
}
