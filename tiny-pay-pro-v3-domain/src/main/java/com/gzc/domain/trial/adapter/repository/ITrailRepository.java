package com.gzc.domain.trial.adapter.repository;


import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;

public interface ITrailRepository {

    SkuVO querySkuVOByGoodsId(String goodsId);

    ActivityDiscountVO queryActivityAndDiscountVOByGoodsId(String goodsId);
}
