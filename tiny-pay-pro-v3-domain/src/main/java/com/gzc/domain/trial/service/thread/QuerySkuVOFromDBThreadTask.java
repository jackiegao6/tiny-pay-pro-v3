package com.gzc.domain.trial.service.thread;

import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.SkuVO;

import java.util.concurrent.Callable;

public class QuerySkuVOFromDBThreadTask implements Callable<SkuVO> {

    private final String goodsId;
    private final ITrailRepository trailRepository;

    public QuerySkuVOFromDBThreadTask(String goodsId, ITrailRepository trailRepository) {
        this.goodsId = goodsId;
        this.trailRepository = trailRepository;
    }

    @Override
    public SkuVO call() throws Exception {
        return trailRepository.querySkuVOByGoodsId(goodsId);
    }
}
