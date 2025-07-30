package com.gzc.domain.trial.service.discount;

import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;

import java.math.BigDecimal;

public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, ActivityDiscountVO.DiscountVO discountVO) {

        // 1. 人群标签过滤
//        if (DiscountTypeEnum.TAG.equals(discountVO.getDiscountType())){
//            boolean isCrowdRange = filterTagId(userId, discountVO.getTagId());
//            if (!isCrowdRange) return originalPrice;
//        }


        // 2. 折扣优惠计算
        return doCalculate(originalPrice, discountVO);
    }

    // 人群过滤 - 限定人群优惠
    private boolean filterTagId(String userId, String tagId) {

        return true;
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, ActivityDiscountVO.DiscountVO groupBuyDiscount);

}
