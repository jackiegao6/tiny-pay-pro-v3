package com.gzc.domain.trial.service.thread;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;

import java.util.concurrent.Callable;

public class QueryActivityAndDiscountVOFromDBThreadTask implements Callable<ActivityDiscountVO> {

    private final String goodsId;
    private final ITrailRepository trailRepository;

    public QueryActivityAndDiscountVOFromDBThreadTask(String goodsId, ITrailRepository trailRepository) {
        this.goodsId = goodsId;
        this.trailRepository = trailRepository;
    }

    @Override
    public ActivityDiscountVO call() throws Exception {
        return trailRepository.queryActivityAndDiscountVOByGoodsId(goodsId);
    }
}
