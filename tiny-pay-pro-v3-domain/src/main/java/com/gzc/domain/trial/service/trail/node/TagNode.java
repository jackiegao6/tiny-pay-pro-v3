package com.gzc.domain.trial.service.trail.node;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.SkuVO;
import com.gzc.domain.trial.service.trail.AbstractNodeSupport;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.domain.trial.service.trail.thread.QueryActivityAndDiscountVOFromDBThreadTask;
import com.gzc.domain.trial.service.trail.thread.QuerySkuVOFromDBThreadTask;
import com.gzc.types.design.framework.tree.NodeHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class TagNode extends AbstractNodeSupport {

    private final TrailNode trailNode;
    private final EndNode endNode;

    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity requestParameter, DynamicContext context) throws Exception {
        ActivityDiscountVO activityDiscountVO = context.getActivityDiscountVO();
        String userId = requestParameter.getUserId();

        String tagId = activityDiscountVO.getTagId();
        if (tagId == null) {
            log.info("活动未配置人群标签信息");
            context.setVisible(true);
            context.setJoin(true);
            return router(requestParameter, context);
        }

        // 活动配置了人群标签
        boolean[] tagRestrictFlag = activityDiscountVO.getTagRestrictFlag();

        boolean isInTagScope = trailRepository.queryInTagScopeByUserId(tagId, userId);
        if(isInTagScope){
            context.setVisible(!tagRestrictFlag[0]);
            context.setJoin(!tagRestrictFlag[1]);
            log.info("当前用户属于人群标签，可见性：{}，参与性{}", context.isVisible(), context.isJoin());
            return router(requestParameter, context);

        }
        context.setVisible(true);
        context.setJoin(true);

        return router(requestParameter, context);
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        if (!context.isVisible()){
            return endNode;
        }
        return trailNode;
    }
}
