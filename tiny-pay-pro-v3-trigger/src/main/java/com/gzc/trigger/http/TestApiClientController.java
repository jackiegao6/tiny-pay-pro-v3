package com.gzc.trigger.http;

import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.NotifyRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/test/")
public class TestApiClientController {

    /**
     * 模拟回调案例
     * <a href="http://127.0.0.1:8091/api/v1/test/group_buy_notify">...</a>
     *
     * @param notifyRequestDTO 通知回调参数
     * @return success 成功，error 失败
     */
    @RequestMapping(value = "group_buy_notify", method = RequestMethod.POST)
    public String groupBuyNotify(@RequestBody NotifyRequestDTO notifyRequestDTO) {
        log.info("模拟测试 第三方服务接收组团结算完成回调 {}", JSON.toJSONString(notifyRequestDTO));
        return "success";
    }

}