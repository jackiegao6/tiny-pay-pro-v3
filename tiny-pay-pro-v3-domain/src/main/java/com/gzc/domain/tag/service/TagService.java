package com.gzc.domain.tag.service;


import com.gzc.domain.tag.adapter.repository.ITagRepository;
import com.gzc.domain.tag.model.entity.resp.CrowdTagsJobEntity;
import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService implements ITagService{

    private final ITagRepository tagRepository;

    @Override
    public void execTagBatchJob(String tagId, String batchId) {

        // 1. 查询批次任务
        CrowdTagsJobEntity crowdTagsJobEntity = tagRepository.queryCrowdTagsJobEntity(tagId, batchId);

        // 2. 采集用户数据 - 这部分需要采集用户的消费类数据，后续有用户发起拼单后再处理。
        // 3. 人群数据
        List<String> userIdList = new ArrayList<>() {{
            add("gao_tag01");
        }};

        // 4. 一般人群标签的处理在公司中，会有专门的数据数仓团队通过脚本方式写入到数据库，就不用这样一个个或者批次来写。
        for (String userId : userIdList) {
            tagRepository.addCrowdTagsUserId(tagId, userId);
        }

        // 5. 更新人群标签统计量
        tagRepository.updateCrowdTagsStatistics(tagId, userIdList.size());

    }
}
