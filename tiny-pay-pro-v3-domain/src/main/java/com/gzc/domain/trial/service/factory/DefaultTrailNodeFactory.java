package com.gzc.domain.trial.service.factory;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.DynamicContext;
import com.gzc.domain.trial.service.node.RootNode;
import com.gzc.types.design.framework.tree.NodeHandler;

public class DefaultTrailNodeFactory<T, D, R> {

    private final RootNode<T, D, R> rootNode;

    public DefaultTrailNodeFactory(RootNode<T, D, R> rootNode) {
        this.rootNode = rootNode;
    }


    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> getRootNode(){
        return rootNode;
    }

}
