package com.gzc.domain.trial.service.node;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.domain.trial.service.AbstractNodeSupport;
import com.gzc.domain.trial.service.DynamicContext;
import com.gzc.domain.trial.service.thread.QueryActivityAndDiscountVOFromDBThreadTask;
import com.gzc.domain.trial.service.thread.QuerySkuVOFromDBThreadTask;
import com.gzc.types.design.framework.tree.AbstractMultiThreadNodeRouter;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrailNode extends AbstractNodeSupport {

    private final EndNode endNode;
    private final ThreadPoolExecutor threadPoolExecutor;// todo


    @Override
    protected void multiThread(TrailMarketProductEntity reqParam, DynamicContext context) throws ExecutionException, InterruptedException, TimeoutException {
        String goodsId = reqParam.getGoodsId();

        FutureTask<SkuVO> skuVOFutureTask = new FutureTask<>(new QuerySkuVOFromDBThreadTask(goodsId, trailRepository));
        threadPoolExecutor.execute(skuVOFutureTask);
        FutureTask<ActivityDiscountVO> activityDiscountVOFutureTask = new FutureTask<>(new QueryActivityAndDiscountVOFromDBThreadTask(goodsId, trailRepository));
        threadPoolExecutor.execute(activityDiscountVOFutureTask);

        SkuVO skuVO = skuVOFutureTask.get(timeout, TimeUnit.SECONDS);
        context.setSkuVO(skuVO);
        if (skuVO == null) log.error("sku 为空");
        ActivityDiscountVO activityDiscountVO = activityDiscountVOFutureTask.get(timeout, TimeUnit.SECONDS);
        context.setActivityDiscountVO(activityDiscountVO);
        if (activityDiscountVO == null) log.error("activityDiscountVO 为空");

    }

    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        log.info("拼团优惠试算 完成");
        return router(reqParam, context);
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return endNode;
    }

}
