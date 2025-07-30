package com.gzc.domain.trial.service.trail.node;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.domain.trial.service.discount.IDiscountCalculateService;
import com.gzc.domain.trial.service.trail.AbstractNodeSupport;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.domain.trial.service.trail.thread.QueryActivityAndDiscountVOFromDBThreadTask;
import com.gzc.domain.trial.service.trail.thread.QuerySkuVOFromDBThreadTask;
import com.gzc.types.design.framework.tree.NodeHandler;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrailNode extends AbstractNodeSupport {

    private final EndNode endNode;
    private final ErrorNode errorNode;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final Map<String, IDiscountCalculateService> discountCalculateServiceMap;


    @Override
    protected void multiThread(TrailMarketProductEntity reqParam, DynamicContext context) throws ExecutionException, InterruptedException, TimeoutException {
        String goodsId = reqParam.getGoodsId();

        FutureTask<SkuVO> skuVOFutureTask = new FutureTask<>(new QuerySkuVOFromDBThreadTask(goodsId, trailRepository));
        threadPoolExecutor.execute(skuVOFutureTask);
        FutureTask<ActivityDiscountVO> activityDiscountVOFutureTask = new FutureTask<>(new QueryActivityAndDiscountVOFromDBThreadTask(goodsId, trailRepository));
        threadPoolExecutor.execute(activityDiscountVOFutureTask);

        SkuVO skuVO = null;
        ActivityDiscountVO activityDiscountVO = null;
        try {
            skuVO = skuVOFutureTask.get(timeout, TimeUnit.SECONDS);
            activityDiscountVO = activityDiscountVOFutureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }
        context.setSkuVO(skuVO);
        context.setActivityDiscountVO(activityDiscountVO);

    }

    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {

        ActivityDiscountVO activityDiscountVO = context.getActivityDiscountVO();
        if (null == activityDiscountVO) {
            return router(reqParam, context);
        }

        ActivityDiscountVO.DiscountVO discountVO = activityDiscountVO.getDiscountVO();
        SkuVO skuVO = context.getSkuVO();

        if (discountVO == null || skuVO == null){
            router(reqParam, context);
        }

        String discountServiceKey = discountVO.getMarketPlan();
        IDiscountCalculateService discountCalculateService = discountCalculateServiceMap.get(discountServiceKey);
        if (null == discountCalculateService) {
            log.info("不存在{}类型的折扣计算服务，支持类型为:{}", discountVO.getMarketPlan(), JSON.toJSONString(discountCalculateServiceMap.keySet()));
            throw new AppException(ResponseCode.WRONG_TYPE.getCode(), ResponseCode.WRONG_TYPE.getInfo());
        }

        BigDecimal currentPrice = discountCalculateService.calculate(reqParam.getUserId(), skuVO.getOriginalPrice(), discountVO);
        context.setCurrentPrice(currentPrice);


        log.info("拼团优惠试算 完成");
        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        if (context.getSkuVO() == null || context.getActivityDiscountVO() == null || context.getActivityDiscountVO().getDiscountVO() == null){
            return errorNode;
        }
        return endNode;
    }

}
