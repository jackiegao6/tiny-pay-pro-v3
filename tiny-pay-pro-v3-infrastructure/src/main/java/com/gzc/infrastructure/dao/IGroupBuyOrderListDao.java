package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IGroupBuyOrderListDao {

    /**
     * 插入拼单明细
     */
    void insert(GroupBuyOrderList groupBuyOrderListReq);


    /**
     * 查询交易记录
     */
    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderListReq);

    Integer queryOrderCountByActivityId(Long activityId, String userId);

}
