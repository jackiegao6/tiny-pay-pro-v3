package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.CrowdTagsDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICrowdTagsDetailDao {

    void addCrowdTagsUserId(CrowdTagsDetail crowdTagsDetailReq);

    Integer queryInTagScopeByUserId(CrowdTagsDetail crowdTagsDetailReq);

}
