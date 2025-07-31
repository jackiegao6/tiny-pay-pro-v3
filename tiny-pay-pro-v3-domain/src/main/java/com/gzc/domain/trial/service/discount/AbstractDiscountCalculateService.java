package com.gzc.domain.trial.service.discount;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Resource
    private ITrailRepository trailRepository;

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, String tagId, ActivityDiscountVO.DiscountVO discountVO) {

        // 1. 人群标签过滤
        if (discountVO.getDiscountType() == 1){
            boolean isInCrowdRange = filterTagId(userId, tagId);
            if (!isInCrowdRange) {
                log.info("折扣优惠计算拦截，用户不再优惠人群标签范围内 userId:{}", userId);
                return originalPrice;
            }
        }

        // 2. 折扣优惠计算
        return doCalculate(originalPrice, discountVO);
    }

    // 人群过滤 - 限定人群优惠
    private boolean filterTagId(String userId, String tagId) {
        return trailRepository.queryInTagScopeByUserId(tagId, userId);
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, ActivityDiscountVO.DiscountVO groupBuyDiscount);

}
