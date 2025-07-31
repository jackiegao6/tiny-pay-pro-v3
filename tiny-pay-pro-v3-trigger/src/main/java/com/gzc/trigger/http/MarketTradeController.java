package com.gzc.trigger.http;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.api.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.resp.MarketPayOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.lock.ITradeOrderService;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.IIndexGroupBuyMarketService;
import com.gzc.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/trade")
@RequiredArgsConstructor
public class MarketTradeController {

    private final IIndexGroupBuyMarketService indexGroupBuyMarketService;
    private final ITradeOrderService tradeOrderService;


    @GetMapping("/lock-order")
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO) throws Exception {

        String userId = lockMarketPayOrderRequestDTO.getUserId();
        String goodsId = lockMarketPayOrderRequestDTO.getGoodsId();
        Long activityId = lockMarketPayOrderRequestDTO.getActivityId();
        String teamId = lockMarketPayOrderRequestDTO.getTeamId();
        String outTradeNo = lockMarketPayOrderRequestDTO.getOutTradeNo();


        // 查询 outTradeNo 是否已经存在交易记录
        MarketPayOrderEntity marketPayOrderEntity = tradeOrderService.queryUnfinishedPayOrderByOutTradeNo(userId, outTradeNo);// todo
        if (marketPayOrderEntity != null) {
            log.info("用户有未完结的订单，交易单号为：{}", outTradeNo);
            LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = LockMarketPayOrderResponseDTO.builder()
                    .orderId(marketPayOrderEntity.getOrderId())
                    .currentPrice(marketPayOrderEntity.getCurrentPrice())
                    .tradeOrderStatus(marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode())
                    .build();

            return Response.<LockMarketPayOrderResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(lockMarketPayOrderResponseDTO)
                    .build();
        }


        // todo 判断拼团锁单是否在有效时间内



        // 判断拼团锁单是否完成了目标
        if (teamId != null) {
            GroupBuyProgressVO groupBuyProgressVO = tradeOrderService.queryGroupBuyProgress(teamId);
            if (groupBuyProgressVO != null && Objects.equals(groupBuyProgressVO.getTargetCount(), groupBuyProgressVO.getLockCount())) {
                log.info("交易锁单拦截-拼单目标已达成:{} {}", userId, teamId);
                return Response.<LockMarketPayOrderResponseDTO>builder()
                        .code(ResponseCode.FULL_TEAM.getCode())
                        .info(ResponseCode.FULL_TEAM.getInfo())
                        .build();
            }
        }


        // 营销优惠试算
        TrailBalanceEntity trialBalanceEntity = indexGroupBuyMarketService.indexMarketTrial(TrailMarketProductEntity.builder()
                .userId(userId)
                .goodsId(goodsId)
                .build());


        marketPayOrderEntity = tradeOrderService.lockMarketPayOrder(userId,
                PayActivityEntity.builder()
                        .teamId(teamId)
                        .activityId(activityId)
                        .startTime(trialBalanceEntity.getStartTime())
                        .endTime(trialBalanceEntity.getEndTime())
                        .targetCount(trialBalanceEntity.getTarget())
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
                        .orderId(marketPayOrderEntity.getOrderId())
                        .teamId(marketPayOrderEntity.getTeamId())
                        .currentPrice(marketPayOrderEntity.getCurrentPrice())
                        .tradeOrderStatus(marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode())
                        .build())
                .build();
    }


}
