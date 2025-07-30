package com.gzc.domain.trial.service.trail;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.factory.DefaultTrailNodeFactory;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IndexGroupBuyMarketService implements IIndexGroupBuyMarketService{

    private final DefaultTrailNodeFactory defaultActivityStrategyFactory;

    @Override
    public TrailBalanceEntity indexMarketTrial(TrailMarketProductEntity trailMarketProductEntity) throws Exception {
        NodeHandler rootNode = defaultActivityStrategyFactory.getRootNode();
        return (TrailBalanceEntity) rootNode.apply(trailMarketProductEntity, new DynamicContext());
    }
}
