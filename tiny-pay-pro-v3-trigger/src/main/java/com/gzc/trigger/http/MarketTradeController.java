package com.gzc.trigger.http;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.api.dto.req.ProductDescRequestDTO;
import com.gzc.api.dto.req.SettlementRequestDTO;
import com.gzc.api.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.api.dto.resp.ProductDescResponseDTO;
import com.gzc.api.dto.resp.SettlementResponseDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.req.TradePaySuccessEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.entity.resp.TradePaySettlementEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.lock.ITradeLockOrderService;
import com.gzc.domain.trade.service.settlement.ITradeSettlementService;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.ITrailService;
import com.gzc.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/trade")
@RequiredArgsConstructor
public class MarketTradeController {

    private final ITrailService trailService;
    private final ITradeLockOrderService tradeLockOrderService;
    private final ITradeSettlementService tradeSettlementService;


    @PostMapping("/lock-order")
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(@RequestBody LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO) throws Exception {

        String userId = lockMarketPayOrderRequestDTO.getUserId();
        String goodsId = lockMarketPayOrderRequestDTO.getGoodsId();
        Long activityId = lockMarketPayOrderRequestDTO.getActivityId();
        String teamId = lockMarketPayOrderRequestDTO.getTeamId();
        String outTradeNo = lockMarketPayOrderRequestDTO.getOutTradeNo();
        String notifyUrl = lockMarketPayOrderRequestDTO.getNotifyUrl();


        // 查询 outTradeNo 是否已经存在交易记录
        LockedOrderEntity lockedOrderEntity = tradeLockOrderService.queryUnfinishedPayOrderByOutTradeNo(userId, outTradeNo);
        if (lockedOrderEntity != null) {
            log.info("用户有未完结的订单，交易单号为：{}", outTradeNo);
            LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = LockMarketPayOrderResponseDTO.builder()
                    .orderId(lockedOrderEntity.getOrderId())
                    .originalPrice(lockedOrderEntity.getOriginalPrice())
                    .deductionPrice(lockedOrderEntity.getDeductionPrice())
                    .currentPrice(lockedOrderEntity.getCurrentPrice())
                    .tradeOrderStatus(lockedOrderEntity.getTradeOrderStatusEnumVO().getCode())
                    .build();

            return Response.<LockMarketPayOrderResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(lockMarketPayOrderResponseDTO)
                    .build();
        }


        // 判断拼团锁单是否完成了目标
        if (teamId != null) {
            GroupBuyProgressVO groupBuyProgressVO = tradeLockOrderService.queryGroupBuyProgress(teamId);
            if (groupBuyProgressVO != null && Objects.equals(groupBuyProgressVO.getTargetCount(), groupBuyProgressVO.getLockCount())) {
                log.info("交易锁单拦截-拼单目标已达成:{} {}", userId, teamId);
                return Response.<LockMarketPayOrderResponseDTO>builder()
                        .code(ResponseCode.FULL_TEAM.getCode())
                        .info(ResponseCode.FULL_TEAM.getInfo())
                        .build();
            }
        }


        // 营销优惠试算
        TrailBalanceEntity trialBalanceEntity = trailService.indexMarketTrial(TrailMarketProductEntity.builder()
                .userId(userId)
                .goodsId(goodsId)
                .build());

        // 锁单
        lockedOrderEntity = tradeLockOrderService.lockMarketPayOrder(userId,
                PayActivityEntity.builder()
                        .teamId(teamId)
                        .activityId(activityId)
                        .startTime(trialBalanceEntity.getStartTime())
                        .endTime(trialBalanceEntity.getEndTime())
                        .validTime(trialBalanceEntity.getValidTime())
                        .targetCount(trialBalanceEntity.getTarget())
                        .notifyUrl(notifyUrl)
                        .build(),
                PayDiscountEntity.builder()
                        .goodsId(goodsId)
                        .goodsName(trialBalanceEntity.getGoodsName())
                        .originalPrice(trialBalanceEntity.getOriginalPrice())
                        .deductionPrice(trialBalanceEntity.getDeductionPrice())
                        .currentPrice(trialBalanceEntity.getCurrentPrice())
                        .outTradeNo(outTradeNo)
                        .build());


        return Response.<LockMarketPayOrderResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(LockMarketPayOrderResponseDTO.builder()
                        .orderId(lockedOrderEntity.getOrderId())
                        .teamId(lockedOrderEntity.getTeamId())
                        .originalPrice(lockedOrderEntity.getOriginalPrice())
                        .deductionPrice(lockedOrderEntity.getDeductionPrice())
                        .currentPrice(lockedOrderEntity.getCurrentPrice())
                        .tradeOrderStatus(lockedOrderEntity.getTradeOrderStatusEnumVO().getCode())
                        .build())
                .build();
    }

    @RequestMapping(value = "/settle-order", method = RequestMethod.POST)
    public Response<SettlementResponseDTO> settlementMarketPayOrder(@RequestBody SettlementRequestDTO requestDTO) {
        try {

            // 1. 结算服务
            TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementService.settlementMarketPayOrder(TradePaySuccessEntity.builder()
                    .userId(requestDTO.getUserId())
                    .outTradeNo(requestDTO.getOutTradeNo())
                    .build());

            SettlementResponseDTO responseDTO = SettlementResponseDTO.builder()
                    .teamId(tradePaySettlementEntity.getTeamId())
                    .build();

            // 返回结果
            Response<SettlementResponseDTO> response = Response.<SettlementResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();

            log.info("营销交易组队结算完成:{} outTradeNo:{} response:{}", requestDTO.getUserId(), requestDTO.getOutTradeNo(), JSON.toJSONString(response));

            return response;
        } catch (Exception e) {
            log.error("营销交易组队结算失败:{} LockMarketPayOrderRequestDTO:{}", requestDTO.getUserId(), JSON.toJSONString(requestDTO), e);
            return Response.<SettlementResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    // todo这一部分在后面更新为商品服务
    @RequestMapping(value = "/get-product", method = RequestMethod.POST)
    public Response<ProductDescResponseDTO> getProduct(@RequestBody ProductDescRequestDTO productDescRequestDTO){
        ProductDescResponseDTO productDescResponseDTO = ProductDescResponseDTO.builder()
                .productName("《手写MyBatis：渐进式源码实践》")
                .productDesc(null)
                .originalPrice(new BigDecimal("100.00"))
                .build();
        return Response.<ProductDescResponseDTO>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(productDescResponseDTO)
                .build();
    }




}
