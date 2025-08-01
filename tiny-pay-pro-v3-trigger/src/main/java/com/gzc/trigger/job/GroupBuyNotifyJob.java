package com.gzc.trigger.job;

import com.alibaba.fastjson.JSON;
import com.gzc.domain.trade.service.notify.ITradeNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description 拼团完结回调通知任务；拼团回调任务表，实际公司场景会定时清理数据结转，不会有太多数据挤压
 */
@Slf4j
@Service
public class GroupBuyNotifyJob {

    @Resource
    private ITradeNotifyService tradeNotifyService;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void exec() {
        try {
            Map<String, Integer> result = tradeNotifyService.execSettlementNotifyJob();
            log.info("定时任务，回调通知某团结算完成任务 result:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("定时任务，回调通知某团结算完成任务失败", e);
        }
    }

}
