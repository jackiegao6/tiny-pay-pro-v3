package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGroupBuyOrderDao {

    /**
     * 插入数据
     */
    void insert(GroupBuyOrder groupBuyOrder);

    /**
     * 更新锁单量
     */
    int updateAddLockCount(String teamId);

    /**
     * 更新锁单量
     */
    int updateSubtractionLockCount(String teamId);

    /**
     * 更新此team下的完成态订单的数量
     */
    int updateOrderCompletedCount(String teamId);

    /**
     * 更新此team下的 拼团状态为全都拼完了
     */
    int updateOrderStatus2COMPLETE(String teamId);

    /**
     * 返回此team下的 GroupBuyOrder
     */
    GroupBuyOrder queryGroupBuyProgressVOByTeamId(String teamId);

}
