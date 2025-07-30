package com.gzc.domain.trial.service.discount.impl;

import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.service.discount.AbstractDiscountCalculateService;
import com.gzc.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("MJ")
public class MJCalculateService extends AbstractDiscountCalculateService {

    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, ActivityDiscountVO.DiscountVO groupBuyDiscount) {

        // 满减表达式 - 100,10 满100减10元
        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);
        BigDecimal x = new BigDecimal(split[0].trim());
        BigDecimal deductionPrice = new BigDecimal(split[1].trim());

        // 不满足最低满减约束，则按照原价
        if (originalPrice.compareTo(x) < 0) {
            return originalPrice;
        }

        // 折扣价格
        BigDecimal currentPrice = originalPrice.subtract(deductionPrice);

        // 判断折扣后金额，最低支付1分钱
        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("0.01");
        }

        return currentPrice;
    }
}
