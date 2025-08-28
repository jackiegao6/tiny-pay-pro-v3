package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IGroupBuyOrderListDao {

    /**
     * 插入明细
     */
    void insert(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 根据 outTradeNo userId status = 0 查询只锁单但未支付用户的明细
     */
    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 查询用户在一个活动上参加的次数
     */
    Integer queryOrderCountByActivityId(Long activityId, String userId);

    /**
     * 根据 outTradeNo userId status = 1 查询锁单+已支付用户的明细
     */
    GroupBuyOrderList queryLockedOrderEntityByOutTradeNo(String userId, String outTradeNo);

    /**
     * 将该用户的明细 从锁单态 至 支付完成态
     */
    int updateOrderListStatus2COMPLETE(GroupBuyOrderList groupBuyOrderListReq);

    /**
     * 返回同一team 下的 所有外部交易单号
     */
    List<String> queryCompletedOutTradeNoListByTeamId(String teamId);

    List<GroupBuyOrderList> queryOwnerInProgressUserDetailList(GroupBuyOrderList groupBuyOrderListReq);

    List<GroupBuyOrderList> queryRandomInProgressUserDetailList(GroupBuyOrderList groupBuyOrderListReq);

    List<GroupBuyOrderList> queryTeamStatisticByActivityId(Long activityId);

    String queryTeamIdByUserIdAndOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);

}
