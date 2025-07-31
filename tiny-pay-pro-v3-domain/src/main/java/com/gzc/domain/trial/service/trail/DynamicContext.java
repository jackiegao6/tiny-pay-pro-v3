package com.gzc.domain.trial.service.trail;


import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DynamicContext {

    private SkuVO skuVO;
    private ActivityDiscountVO activityDiscountVO;
    private BigDecimal deductionPrice;
    private BigDecimal currentPrice;
    private boolean isVisible;
    private boolean isJoin;
}
