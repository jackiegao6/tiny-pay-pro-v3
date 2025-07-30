package com.gzc.domain.trial.service.discount;

import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;

import java.math.BigDecimal;

public interface IDiscountCalculateService {

    /**
     * 折扣计算
     *
     * @param userId           用户ID 做人群标签的过滤使用
     * @param originalPrice    商品原始价格
     * @param discountVO 折扣计划配置
     * @return 商品优惠价格
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice, ActivityDiscountVO.DiscountVO discountVO);


}
