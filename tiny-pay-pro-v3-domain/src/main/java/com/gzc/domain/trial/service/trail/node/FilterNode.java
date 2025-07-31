package com.gzc.domain.trial.service.trail.node;

import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.model.valobj.ActivityDiscountVO;
import com.gzc.domain.trial.model.valobj.ActivityStatusEnumVO;
import com.gzc.domain.trial.service.trail.AbstractNodeSupport;
import com.gzc.domain.trial.service.trail.DynamicContext;
import com.gzc.domain.trial.service.trail.thread.QueryActivityAndDiscountVOFromDBThreadTask;
import com.gzc.types.design.framework.tree.NodeHandler;
import com.gzc.types.enums.ResponseCode;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FilterNode extends AbstractNodeSupport {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final TagNode tagNode;


    @Override
    protected void multiThread(TrailMarketProductEntity reqParam, DynamicContext context) throws ExecutionException, InterruptedException, TimeoutException {
        String goodsId = reqParam.getGoodsId();

        FutureTask<ActivityDiscountVO> activityDiscountVOFutureTask = new FutureTask<>(new QueryActivityAndDiscountVOFromDBThreadTask(goodsId, trailRepository));
        threadPoolExecutor.execute(activityDiscountVOFutureTask);

        ActivityDiscountVO activityDiscountVO = null;
        try {
            activityDiscountVO = activityDiscountVOFutureTask.get(timeout, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }
        context.setActivityDiscountVO(activityDiscountVO);
    }

    @Override
    protected TrailBalanceEntity doApply(TrailMarketProductEntity requestParameter, DynamicContext context) throws Exception {
        ActivityDiscountVO activityDiscountVO = context.getActivityDiscountVO();
        Integer status = activityDiscountVO.getStatus();

        // 校验；活动状态 - 可以抛业务异常code，或者把code写入到动态上下文dynamicContext中，最后获取。
        if (!ActivityStatusEnumVO.EFFECTIVE.getCode().equals(status)){
            log.info("活动的可用性校验，活动未生效");
            throw new AppException(ResponseCode.NO_EFFECTIVE.getInfo());
        }

        // 校验；活动时间
        Date currentTime = new Date();
        Date startTime = activityDiscountVO.getStartTime();
        Date endTime = activityDiscountVO.getEndTime();
        Long activityId = activityDiscountVO.getActivityId();
        if (currentTime.before(startTime) || currentTime.after(endTime)) {
            log.info("活动的可用性校验，非可参与时间范围 activityId:{}", activityId);
            throw new AppException(ResponseCode.NOT_IN_VALID_DATE.getInfo());
        }

        // 校验；用户参与次数限制
        // 查询用户在一个拼团活动上参与的次数
        String userId = requestParameter.getUserId();
        Integer limitCount = activityDiscountVO.getTakeLimitCount();
        Integer count = trailRepository.queryOrderCountByActivityId(activityId, userId);
        if (count >= limitCount){
            log.info("用户参与次数校验，已达可参与上限 activityId:{}", activityId);
            throw new AppException(ResponseCode.LIMIT_ENABLED.getInfo());
        }

        return router(requestParameter, context);
    }

    @Override
    public NodeHandler<TrailMarketProductEntity, DynamicContext, TrailBalanceEntity> get(TrailMarketProductEntity reqParam, DynamicContext context) throws Exception {
        return tagNode;
    }
}
