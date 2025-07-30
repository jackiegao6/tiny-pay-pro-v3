package com.gzc.domain.trial.service.trail.node;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.AbstractNodeSupport;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.types.design.framework.tree.NodeHandler;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ErrorNode extends AbstractNodeSupport {
    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity requestParameter, DynamicContext dynamicContext) throws Exception {
        // 无营销配置
        log.info("商品无拼团营销配置 {}", requestParameter.getGoodsId());
        throw new AppException(ResponseCode.UNKNOWN_ERROR.getCode(), ResponseCode.UNKNOWN_ERROR.getInfo());

    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return defaultHandler;
    }
}
