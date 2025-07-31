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
        lockMarketPayOrderRequestDTO.setUserId("gaogao3");
        lockMarketPayOrderRequestDTO.setTeamId(null);
        lockMarketPayOrderRequestDTO.setActivityId(100123L);
        lockMarketPayOrderRequestDTO.setGoodsId("9890001");
        lockMarketPayOrderRequestDTO.setOutTradeNo(RandomStringUtils.randomNumeric(12));
        Response<LockMarketPayOrderResponseDTO> lockMarketPayOrderResponseDTOResponse = marketTradeController.lockMarketPayOrder(lockMarketPayOrderRequestDTO);
        log.info("测试结果 req:{} \nres:{}", JSON.toJSONString(lockMarketPayOrderRequestDTO), JSON.toJSONString(lockMarketPayOrderResponseDTOResponse));
    }


    @Test
    public void test_lockMarketPayOrder_teamId_not_null() throws Exception {
        LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO = new LockMarketPayOrderRequestDTO();
        lockMarketPayOrderRequestDTO.setUserId("gaogao5");
        lockMarketPayOrderRequestDTO.setTeamId("93526380");
        lockMarketPayOrderRequestDTO.setActivityId(100123L);
        lockMarketPayOrderRequestDTO.setGoodsId("9890001");
        lockMarketPayOrderRequestDTO.setOutTradeNo(RandomStringUtils.randomNumeric(12));
        Response<LockMarketPayOrderResponseDTO> lockMarketPayOrderResponseDTOResponse = marketTradeController.lockMarketPayOrder(lockMarketPayOrderRequestDTO);
        log.info("测试结果 req:{} \nres:{}", JSON.toJSONString(lockMarketPayOrderRequestDTO), JSON.toJSONString(lockMarketPayOrderResponseDTOResponse));
    }

    @Test
    public void test_settlementMarketPayOrder() {
        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity();
        tradePaySuccessEntity.setUserId("gao");
        tradePaySuccessEntity.setOutTradeNo("876487474864");
        tradePaySuccessEntity.setTeamId("42700934");
        TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementService.settlementMarketPayOrder(tradePaySuccessEntity);
        log.info("请求参数:{}", JSON.toJSONString(tradePaySuccessEntity));
        log.info("测试结果:{}", JSON.toJSONString(tradePaySettlementEntity));
    }
}
