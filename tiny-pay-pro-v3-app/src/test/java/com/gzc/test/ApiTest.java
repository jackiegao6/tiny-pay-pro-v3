package com.gzc.test;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trial.adapter.repository.ITrailRepository;
import com.gzc.domain.trial.model.valobj.SkuVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private ITrailRepository trailRepository;

    @Test
    public void test_trailRepository() {
        String goodsId = "9890001";
        SkuVO skuVO = trailRepository.querySkuVOByGoodsId(goodsId);
        log.info(JSON.toJSONString(skuVO));
        log.info("测试完成");
    }

    @Test
    public void test_trailRepository_2() {
        String tagId = "RQ_KJHKL98UU78H66554GFDV";
        String userId = "gzc";

        boolean res = trailRepository.queryInTagScopeByUserId(tagId, userId);
        log.info(String.valueOf(res));
        log.info("测试完成");
    }




}
