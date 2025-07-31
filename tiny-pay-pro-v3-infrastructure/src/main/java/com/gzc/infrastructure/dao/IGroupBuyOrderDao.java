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
     * 查询进度
     */
    GroupBuyOrder queryGroupBuyProgress(String teamId);

}
