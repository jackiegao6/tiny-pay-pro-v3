package com.gzc.infrastructure.adapter.port;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trade.adapter.port.ITradePort;
import com.gzc.domain.trade.model.valobj.NotifyConfigEnumVO;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;
import com.gzc.domain.trade.model.valobj.TeamVO;
import com.gzc.infrastructure.gateway.GroupBuyNotifyService;
import com.gzc.infrastructure.mq.EventPublisher;
import com.gzc.infrastructure.redis.IRedisService;
import com.gzc.types.enums.NotifyTaskHTTPEnumVO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class TradePort implements ITradePort {

    private final GroupBuyNotifyService groupBuyNotifyService;
    private final IRedisService redisService;

    private final EventPublisher eventPublisher;

    @Override
    public String settlementFinishNotify(NotifyTaskVO notifyTaskVO) throws Exception {

        RLock lock = redisService.getLock(notifyTaskVO.lockKey());

        if (lock.tryLock(3,0, TimeUnit.SECONDS))
            try {
                if (NotifyConfigEnumVO.HTTP.getCode().equals(notifyTaskVO.getNotifyType())){
                    // HTTP 回调
                    return groupBuyNotifyService.settlementFinishNotify(notifyTaskVO.getNotifyUrl(), notifyTaskVO.getParameterJson());
                }
                if(NotifyConfigEnumVO.MQ.getCode().equals(notifyTaskVO.getNotifyType())){
                    // MQ 回调
                    eventPublisher.publish(notifyTaskVO.getNotifyMQ(), notifyTaskVO.getParameterJson());
                    return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                }
                return NotifyTaskHTTPEnumVO.ERROR.getCode();
            } finally {
                if (lock.isLocked() && lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        else {
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }
    }

    @Override
    public String teamFinishNotify(TeamVO teamVO) {
        return groupBuyNotifyService.teamFinishNotify(JSON.toJSONString(teamVO));
    }
}
