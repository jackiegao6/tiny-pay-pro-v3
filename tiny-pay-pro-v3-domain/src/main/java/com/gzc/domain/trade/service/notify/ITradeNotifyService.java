package com.gzc.domain.trade.service.notify;

import java.util.Map;

public interface ITradeNotifyService {
    /**
     * 结算完成后 回调通知 其他微服务
     *
     * @return 结算数量
     * @throws Exception 异常
     */
    Map<String, Integer> execSettlementNotifyJob() throws Exception;

    /**
     * 可以指定给某个拼团队伍做回调处理
     *
     * @param teamId 指定结算组ID
     * @return 结算数量
     * @throws Exception 异常
     */
    Map<String, Integer> execSettlementNotifyJob(String teamId) throws Exception;

}
