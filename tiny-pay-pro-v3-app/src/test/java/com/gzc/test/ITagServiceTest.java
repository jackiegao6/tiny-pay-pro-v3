package com.gzc.test;


import com.gzc.domain.tag.service.ITagService;
import com.gzc.domain.tag.service.TagService;
import com.gzc.infrastructure.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBitSet;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ITagServiceTest {


    @Resource
    private ITagService tagService;
    @Resource
    private IRedisService redisService;


    // 测试之前应该由运营将所有有关的用户 涌入到对应的人群标签里
    @Test
    public void test_tag_job() {
        tagService.execTagBatchJob("test_tag", "10002");
    }

    @Test
    public void test_get_tag_bitmap() {
        RBitSet bitSet = redisService.getBitSet("test_tag");
        // 是否存在
        log.info("gao_tag 存在，预期结果为 true，测试结果:{}", bitSet.get(redisService.getIndexFromUserId("gao_tag")));
        log.info("gaogao_tag 存在，预期结果为 true，测试结果:{}", bitSet.get(redisService.getIndexFromUserId("gaogao_tag")));
        log.info("gzcc 不存在，预期结果为 false，测试结果:{}", bitSet.get(redisService.getIndexFromUserId("gzcc")));
    }
}
