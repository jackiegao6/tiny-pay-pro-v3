package com.gzc.domain.trade.service.notify;

import com.gzc.domain.trade.adapter.port.ITradePort;
import com.gzc.domain.trade.adapter.repository.ITradeRepository;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;
import com.gzc.types.enums.NotifyTaskHTTPEnumVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class TradeNotifyService implements ITradeNotifyService{

    private final ITradeRepository tradeRepository;
    private final ITradePort tradePort;

    @Override
    public Map<String, Integer> execSettlementNotifyJob() throws Exception {
        log.info("拼团交易-执行结算通知任务");

        // 查询未执行任务
        List<NotifyTaskVO> notifyTaskEntityList = tradeRepository.queryUnExecutedNotifyTaskList();
        return execSettlementNotifyJob(notifyTaskEntityList);
    }

    @Override
    public Map<String, Integer> execSettlementNotifyJob(String teamId) throws Exception {
        log.info("回调通知，指定 teamId:{}", teamId);

        List<NotifyTaskVO> notifyTaskEntityList = tradeRepository.queryUnExecutedNotifyTaskList(teamId);
        return execSettlementNotifyJob(notifyTaskEntityList);
    }

    @Override
    public Map<String, Integer> execSettlementNotifyJob(NotifyTaskVO notifyTaskVO) throws Exception {
        List<NotifyTaskVO> notifyTaskVOS = Collections.singletonList(notifyTaskVO);
        return execSettlementNotifyJob(notifyTaskVOS);
    }

    private Map<String, Integer> execSettlementNotifyJob(List<NotifyTaskVO> notifyTaskEntityList) throws Exception {

        int successCount = 0, errorCount = 0, retryCount = 0;
        for (NotifyTaskVO notifyTask : notifyTaskEntityList) {
            // 回调处理 success 成功，error 失败
            String response = tradePort.settlementFinishNotify(notifyTask);

            // 更新状态判断&变更数据库表回调任务状态
            if (NotifyTaskHTTPEnumVO.SUCCESS.getCode().equals(response)) {
                int updateCount = tradeRepository.updateNotifyTaskStatusSuccess(notifyTask.getTeamId());
                if (updateCount == 1) {
                    successCount += 1;
                }
            }

            else if (NotifyTaskHTTPEnumVO.ERROR.getCode().equals(response)) {
                // 回调任务出错
                if (notifyTask.getNotifyCount() < 3) {
                    // 回调任务最多重试3次
                    int updateCount = tradeRepository.updateNotifyTaskStatusRetry(notifyTask.getTeamId());
                    if (1 == updateCount) {
                        retryCount += 1;
                    }
                }
                else {
                    // 回调任务第四次更新回调任务为失败
                    int updateCount = tradeRepository.updateNotifyTaskStatusError(notifyTask.getTeamId());
                    if (1 == updateCount) {
                        errorCount += 1;
                    }
                }
            }
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("waitCount", notifyTaskEntityList.size());
        resultMap.put("successCount", successCount);
        resultMap.put("retryCount", retryCount);
        resultMap.put("errorCount", errorCount);

        return resultMap;
    }
}
