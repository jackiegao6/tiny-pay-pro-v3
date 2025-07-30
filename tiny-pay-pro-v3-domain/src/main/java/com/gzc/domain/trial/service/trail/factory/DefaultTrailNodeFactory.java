package com.gzc.domain.trial.service.trail.factory;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.domain.trial.service.trail.node.RootNode;
import com.gzc.types.design.framework.tree.NodeHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultTrailNodeFactory {

    private final RootNode rootNode;

    public DefaultTrailNodeFactory(RootNode rootNode) {
        this.rootNode = rootNode;
    }


    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> getRootNode(){
        return rootNode;
    }

}
