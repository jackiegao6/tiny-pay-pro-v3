package com.gzc.domain.trial.service.node;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.AbstractNodeSupport;
import com.gzc.domain.trial.service.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RootNode<T, D, R> extends AbstractNodeSupport {

    private final SwitchNode<T, D, R> switchNode;

    @Override
    public TrailBalanceEntity apply(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        log.info("RootNode...");
        return null;
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return switchNode;
    }
}
