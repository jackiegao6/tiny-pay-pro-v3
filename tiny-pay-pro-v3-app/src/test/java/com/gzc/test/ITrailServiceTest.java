package com.gzc.test;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.IIndexGroupBuyMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ITrailServiceTest {

    @Resource
    private IIndexGroupBuyMarketService iIndexGroupBuyMarketService;

    @Test
    public void test_indexMarketTrial() throws Exception {

        TrailMarketProductEntity trailMarketProductEntity = new TrailMarketProductEntity();
        trailMarketProductEntity.setUserId("gzc");
        trailMarketProductEntity.setGoodsId("9890001");

        TrailBalanceEntity trailBalanceEntity = iIndexGroupBuyMarketService.indexMarketTrial(trailMarketProductEntity);

        log.info("返回结果:{}", JSON.toJSONString(trailBalanceEntity));
    }
}
