package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.infrastructure.dao.IGroupBuyActivityDao;
import com.gzc.infrastructure.dao.IGroupBuyDiscountDao;
import com.gzc.infrastructure.dao.ISkuDao;
import com.gzc.infrastructure.dao.po.GroupBuyActivity;
import com.gzc.infrastructure.dao.po.GroupBuyDiscount;
import com.gzc.infrastructure.dao.po.Sku;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrailRepository implements ITrailRepository {

    private final ISkuDao skuDao;
    private final IGroupBuyActivityDao activityDao;
    private final IGroupBuyDiscountDao discountDao;

    @Override
    public SkuVO querySkuVOByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);

        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public ActivityDiscountVO queryActivityAndDiscountVOByGoodsId(String goodsId) {
        GroupBuyActivity groupBuyActivity = activityDao.queryActivityByGoodsId(goodsId);
        String discountId = groupBuyActivity.getDiscountId();
        GroupBuyDiscount groupBuyDiscount = discountDao.queryDiscountByDiscountId(discountId);
        return ActivityDiscountVO.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .goodsId(goodsId)
                .discountVO(ActivityDiscountVO.DiscountVO.builder()
                        .discountName(groupBuyDiscount.getDiscountName())
                        .discountDesc(groupBuyDiscount.getDiscountDesc())
                        .discountType(groupBuyDiscount.getDiscountType())
                        .marketPlan(groupBuyDiscount.getMarketPlan())
                        .marketExpr(groupBuyDiscount.getMarketExpr())
                        .build())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(groupBuyActivity.getStatus())
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();
    }
}
