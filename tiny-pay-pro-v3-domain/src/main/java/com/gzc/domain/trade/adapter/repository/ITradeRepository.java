package com.gzc.domain.trade.adapter.repository;

import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.model.valobj.NotifyTaskVO;

import java.util.List;

public interface ITradeRepository {


    LockedOrderEntity queryUnfinishedPayOrderByOutTradeNo(String userId, String outTradeNo);

    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    LockedOrderEntity lockOrderProcess(String userId, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity);

    GroupBuyProgressVO querygroupBuyProgressVOByTeamId(String teamId);

    void settlementProcess(TradePaySuccessEntity tradePaySuccessEntity, GroupBuyProgressVO groupBuyProgressVO);

    List<NotifyTaskVO> queryUnExecutedNotifyTaskList();

    List<NotifyTaskVO> queryUnExecutedNotifyTaskList(String teamId);

    int updateNotifyTaskStatusSuccess(String teamId);

    int updateNotifyTaskStatusError(String teamId);

    int updateNotifyTaskStatusRetry(String teamId);

}
