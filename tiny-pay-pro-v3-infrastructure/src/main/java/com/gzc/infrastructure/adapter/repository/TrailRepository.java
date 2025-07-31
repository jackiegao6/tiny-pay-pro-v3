package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.infrastructure.dao.*;
import com.gzc.infrastructure.dao.po.CrowdTagsDetail;
import com.gzc.infrastructure.dao.po.GroupBuyActivity;
import com.gzc.infrastructure.dao.po.GroupBuyDiscount;
import com.gzc.infrastructure.dao.po.Sku;
import com.gzc.infrastructure.redis.IRedisService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBitSet;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrailRepository implements ITrailRepository {

    private final ISkuDao skuDao;
    private final IGroupBuyActivityDao activityDao;
    private final IGroupBuyDiscountDao discountDao;
    private final ICrowdTagsDetailDao crowdTagsDetailDao;
    private final IGroupBuyOrderListDao orderListDao;
    private final IRedisService redisService;


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


    @Override
    public boolean queryInTagScopeByUserId(String tagId, String userId) {
        // 先查找redis 的 bitMap
        RBitSet bitSet = redisService.getBitSet(tagId);
        boolean isFound = bitSet.get(redisService.getIndexFromUserId(userId));
        if (isFound){
            return true;
        }

        // redis查不到就查 数据库
        CrowdTagsDetail crowdTagsDetailReq = CrowdTagsDetail.builder()
                .tagId(tagId)
                .userId(userId)
                .build();
        Integer resNum  = crowdTagsDetailDao.queryInTagScopeByUserId(crowdTagsDetailReq);
        return resNum != 0;
    }


    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {
        return orderListDao.queryOrderCountByActivityId(activityId, userId);
    }
}
