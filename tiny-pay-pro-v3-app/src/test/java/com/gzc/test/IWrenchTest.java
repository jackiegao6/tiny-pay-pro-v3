package com.gzc.test;

import com.gzc.dynamic.config.center.domain.model.valobj.AttributeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IWrenchTest {

    @Resource
    private RTopic dynamicConfigCenterRedisTopic;

    @Test
    public void test_openCache(){
        dynamicConfigCenterRedisTopic.publish(new AttributeVO("cacheOpenSwitch","1"));
    }
}
