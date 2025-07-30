package com.gzc.domain.trial.service.node;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.domain.trial.service.AbstractNodeSupport;
import com.gzc.domain.trial.service.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class EndNode extends AbstractNodeSupport {


    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity requestParameter, DynamicContext dynamicContext) throws Exception {
        SkuVO skuVO = dynamicContext.getSkuVO();
        ActivityDiscountVO activityDiscountVO = dynamicContext.getActivityDiscountVO();


        return TrailBalanceEntity.builder()
                .goodsId(skuVO.getGoodsId())
                .goodsName(skuVO.getGoodsName())
                .originalPrice(skuVO.getOriginalPrice())
                .deductionPrice(new BigDecimal("0.00"))
                .currentPrice(new BigDecimal("0.00"))
                .target(activityDiscountVO.getTarget())
                .startTime(activityDiscountVO.getStartTime())
                .endTime(activityDiscountVO.getEndTime())
                .isVisible(true)//todo
                .isEnable(true)
                .build();
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return defaultHandler;
    }
}
