package com.gzc.domain.trade.model.entity.resp;

import com.gzc.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description 拼团，预购订单营销实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LockedOrderEntity {

    /** 预购订单ID */
    private String orderId;
    private String teamId;
    private BigDecimal originalPrice;
    private BigDecimal deductionPrice;
    private BigDecimal currentPrice;
    /** 交易订单状态枚举 */
    private TradeOrderStatusEnumVO tradeOrderStatusEnumVO;

}
