package com.gzc.trigger.http;


import com.gzc.api.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.api.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.trade.model.entity.req.PayActivityEntity;
import com.gzc.domain.trade.model.entity.req.PayDiscountEntity;
import com.gzc.domain.trade.model.entity.resp.LockedOrderEntity;
import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import com.gzc.domain.trade.service.lock.ITradeLockOrderService;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.ITrailService;
import com.gzc.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/trade")
@RequiredArgsConstructor
public class MarketTradeController {

    private final ITrailService trailService;
    private final ITradeLockOrderService tradeLockOrderService;


    @GetMapping("/lock-order")
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO) throws Exception {

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
                        .currentPrice(lockedOrderEntity.getCurrentPrice())
                        .tradeOrderStatus(lockedOrderEntity.getTradeOrderStatusEnumVO().getCode())
                        .build())
                .build();
    }


}
