package com.gzc.infrastructure.adapter.port;

import com.gzc.domain.trade.adapter.port.ITradePort;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;
import com.gzc.infrastructure.gateway.GroupBuyNotifyService;
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

    @Override
    public String settlementFinishNotify(NotifyTaskVO notifyTaskVO) throws Exception {

        RLock lock = redisService.getLock(notifyTaskVO.lockKey());

        if (lock.tryLock(3,0, TimeUnit.SECONDS))
            try {
                return groupBuyNotifyService.settlementFinishNotify(notifyTaskVO.getNotifyUrl(), notifyTaskVO.getParameterJson());
            } finally {
                if (lock.isLocked() && lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        else {
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }

    }
}
