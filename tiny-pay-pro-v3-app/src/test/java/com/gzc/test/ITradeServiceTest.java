package com.gzc.test;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.api.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.service.settlement.ITradeSettlementService;
import com.gzc.trigger.http.MarketTradeController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITradeServiceTest {

    @Resource
    private MarketTradeController marketTradeController;
    @Resource
    private ITradeSettlementService tradeSettlementService;

    @Test
    public void test_lockMarketPayOrder() throws Exception {
        LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO = new LockMarketPayOrderRequestDTO();
        lockMarketPayOrderRequestDTO.setUserId("xiaoming1");
        lockMarketPayOrderRequestDTO.setTeamId(null);
        lockMarketPayOrderRequestDTO.setActivityId(100123L);
        lockMarketPayOrderRequestDTO.setGoodsId("9890001");
        lockMarketPayOrderRequestDTO.setOutTradeNo(RandomStringUtils.randomNumeric(12));
        lockMarketPayOrderRequestDTO.setNotifyUrl("http://localhost:8091/test");
        Response<LockMarketPayOrderResponseDTO> lockMarketPayOrderResponseDTOResponse = marketTradeController.lockMarketPayOrder(lockMarketPayOrderRequestDTO);
        log.info("测试结果 req:{} \nres:{}", JSON.toJSONString(lockMarketPayOrderRequestDTO), JSON.toJSONString(lockMarketPayOrderResponseDTOResponse));
    }


    @Test
    public void test_lockMarketPayOrder_teamId_not_null() throws Exception {
        LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO = new LockMarketPayOrderRequestDTO();
        lockMarketPayOrderRequestDTO.setUserId("xiaoming3");
        lockMarketPayOrderRequestDTO.setTeamId("77491443");
        lockMarketPayOrderRequestDTO.setActivityId(100123L);
        lockMarketPayOrderRequestDTO.setGoodsId("9890001");
        lockMarketPayOrderRequestDTO.setOutTradeNo(RandomStringUtils.randomNumeric(12));
        lockMarketPayOrderRequestDTO.setNotifyUrl("http://localhost:8091/test");
        Response<LockMarketPayOrderResponseDTO> lockMarketPayOrderResponseDTOResponse = marketTradeController.lockMarketPayOrder(lockMarketPayOrderRequestDTO);
        log.info("测试结果 req:{} \nres:{}", JSON.toJSONString(lockMarketPayOrderRequestDTO), JSON.toJSONString(lockMarketPayOrderResponseDTOResponse));
    }

    @Test
    public void test_settlementMarketPayOrder() throws Exception {
        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity();
        tradePaySuccessEntity.setUserId("xiaoming2");
        tradePaySuccessEntity.setTeamId("77491443");
        tradePaySuccessEntity.setOutTradeNo("337736036119");
        TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementService.settlementMarketPayOrder(tradePaySuccessEntity);
        log.info("请求参数:{}", JSON.toJSONString(tradePaySuccessEntity));
        log.info("测试结果:{}", JSON.toJSONString(tradePaySettlementEntity));
    }
}
