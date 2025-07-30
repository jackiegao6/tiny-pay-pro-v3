package com.gzc.domain.trial.service;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.types.design.framework.tree.AbstractMultiThreadNodeRouter;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractNodeSupport extends AbstractMultiThreadNodeRouter<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> {

    protected long timeout = 500;

    @Resource
    protected ITrailRepository trailRepository;


    @Override
    protected void multiThread(TrailMarketProductEntity requestParameter, DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }
}
