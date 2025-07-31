package com.gzc.domain.trial.service.trail.node;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.domain.trial.service.trail.AbstractNodeSupport;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
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
        BigDecimal currentPrice = dynamicContext.getCurrentPrice();
        BigDecimal deductionPrice = dynamicContext.getDeductionPrice();

        return TrailBalanceEntity.builder()
                .goodsId(skuVO != null ? skuVO.getGoodsId() : null)
                .goodsName(skuVO != null ? skuVO.getGoodsName() : null)
                .originalPrice(skuVO != null ? skuVO.getOriginalPrice() : null)
                .deductionPrice(deductionPrice)
                .currentPrice(currentPrice)
                .target(activityDiscountVO.getTarget())
                .startTime(activityDiscountVO.getStartTime())
                .endTime(activityDiscountVO.getEndTime())
                .isVisible(dynamicContext.isVisible())
                .isEnable(dynamicContext.isJoin())
                .build();
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return defaultHandler;
    }
}
