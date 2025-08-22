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
        BigDecimal originalPrice = skuVO.getOriginalPrice();
        BigDecimal currentPrice = dynamicContext.getCurrentPrice();
        BigDecimal deductionPrice = dynamicContext.getDeductionPrice();

        return TrailBalanceEntity.builder()
                .goodsId(requestParameter.getGoodsId())
                .goodsName(skuVO.getGoodsName())
                .originalPrice(originalPrice)
                .deductionPrice(deductionPrice == null ? new BigDecimal("0") : deductionPrice)
                .currentPrice(currentPrice == null ? originalPrice : currentPrice)
                .target(activityDiscountVO.getTarget())
                .startTime(activityDiscountVO.getStartTime())
                .endTime(activityDiscountVO.getEndTime())
                .validTime(activityDiscountVO.getValidTime())
                .isVisible(dynamicContext.isVisible())
                .isEnable(dynamicContext.isJoin())
                .activityId(activityDiscountVO.getActivityId())
                .build();
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return defaultHandler;
    }
}
