package com.gzc.domain.trial.service;


import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DynamicContext {

    private SkuVO skuVO;
    private ActivityDiscountVO activityDiscountVO;
}
