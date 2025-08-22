package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.tag.adapter.repository.ITagRepository;
import com.gzc.domain.tag.model.entity.resp.CrowdTagsJobEntity;
import com.gzc.infrastructure.dao.ICrowdTagsDao;
import com.gzc.infrastructure.dao.ICrowdTagsDetailDao;
import com.gzc.infrastructure.dao.ICrowdTagsJobDao;
import com.gzc.infrastructure.dao.po.CrowdTags;
import com.gzc.infrastructure.dao.po.CrowdTagsDetail;
import com.gzc.infrastructure.dao.po.CrowdTagsJob;
import com.gzc.infrastructure.redis.IRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBitSet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
@Slf4j
public class TagRepository implements ITagRepository {

    private final ICrowdTagsDetailDao crowdTagsDetailDao;
    private final ICrowdTagsJobDao crowdTagsJobDao;
    private final ICrowdTagsDao crowdTagsDao;
    private final IRedisService redisService;

    /**
     * 根据标签Id 和 批次Id 得到 批次任务对象
     *
     * @param tagId   标签Id
     * @param batchId 批次Id
     * @return 批次任务对象
     */
    @Override
    public CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJobReq = CrowdTagsJob.builder()
                .tagId(tagId)
                .batchId(batchId)
                .build();

        CrowdTagsJob crowdTagsJobRes = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJobReq);
        if (crowdTagsJobRes == null) return null;
        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJobRes.getTagType())
                .tagRule(crowdTagsJobRes.getTagRule())
                .statStartTime(crowdTagsJobRes.getStatStartTime())
                .statEndTime(crowdTagsJobRes.getStatEndTime())
                .build();
    }


    /**
     * 记录标签Id 和 用户Id 的关系
     * 到数据库 和 缓存
     */
    @Override
    public void addCrowdTagsUserId(String tagId, String userId) {
        CrowdTagsDetail crowdTagsDetailReq = new CrowdTagsDetail();
        crowdTagsDetailReq.setTagId(tagId);
        crowdTagsDetailReq.setUserId(userId);

        try {
            crowdTagsDetailDao.addCrowdTagsUserId(crowdTagsDetailReq);

            // 获取BitSet
            RBitSet tagBitSet = redisService.getBitSet("tag_bitmap:" +tagId);
            tagBitSet.set(redisService.getIndexFromUserId(userId), true);
        } catch (DuplicateKeyException ignore) {
            // 忽略唯一索引冲突
            log.error("唯一索引冲突");
        }
    }


    @Override
    public void updateCrowdTagsStatistics(String tagId, int size) {
        CrowdTags crowdTagsReq = CrowdTags.builder()
                .tagId(tagId)
                .statistics(size)
                .build();
        crowdTagsDao.updateCrowdTagsStatistics(crowdTagsReq);
    }
}
