package com.gzc.domain.trial.service;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.factory.DefaultTrailNodeFactory;
import com.gzc.domain.trial.service.node.RootNode;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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
