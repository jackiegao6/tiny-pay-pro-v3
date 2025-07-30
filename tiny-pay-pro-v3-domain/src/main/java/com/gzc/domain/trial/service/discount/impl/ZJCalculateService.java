package com.gzc.domain.trial.service.discount.impl;

import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service("ZJ")
public class ZJCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, ActivityDiscountVO.DiscountVO groupBuyDiscount) {
        String marketExpr = groupBuyDiscount.getMarketExpr();
        BigDecimal deductionPrice = new BigDecimal(marketExpr);

        BigDecimal currentPrice = originalPrice.subtract(deductionPrice);

        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return currentPrice;
    }
}
