package com.gzc.domain.trial.adapter.repository;


import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;

public interface ITrailRepository {

    ActivityDiscountVO queryActivityAndDiscountVOByGoodsId(String goodsId);

    boolean queryInTagScopeByUserId(String tagId, String userId);

    Integer queryOrderCountByActivityId(Long activityId, String userId);

    SkuVO querySkuVOByGoodsId(String goodsId);

}
