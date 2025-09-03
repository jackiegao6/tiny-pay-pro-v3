package com.gzc.infrastructure.dcc;

import com.gzc.dynamic.config.center.types.annotation.DCCValue;
import org.springframework.stereotype.Service;

@Service
public class DCCService {

    @DCCValue(value = "cacheOpenSwitch:0")
    private String cacheOpenSwitch;

    /**
     * 缓存开启开关，true为开启，1为开启
     */
    public boolean isCacheOpen(){
        return "1".equals(cacheOpenSwitch);
    }

}
