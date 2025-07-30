package com.gzc.domain.tag.adapter.repository;

import com.gzc.domain.tag.model.entity.resp.CrowdTagsJobEntity;

public interface ITagRepository {

    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    void addCrowdTagsUserId(String tagId, String userId);

    void updateCrowdTagsStatistics(String tagId, int size);

}
