package com.gzc.domain.trade.model.valobj;

import lombok.*;

/**
 * @description 交易订单状态枚举
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TradeOrderStatusEnumVO {

    CREATE(0, "初始创建"),
    COMPLETE(1, "消费完成"),
    CLOSE(2, "超时关单"),
    ;

    private Integer code;
    private String info;

    public static TradeOrderStatusEnumVO valueOf(Integer code) {
        return switch (code) {
            case 1 -> COMPLETE;
            case 2 -> CLOSE;
            default -> CREATE;
        };
    }

}
