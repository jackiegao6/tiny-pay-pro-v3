package com.gzc.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(0, "成功"),
    UN_ERROR(1, "未知失败"),
    ILLEGAL_PARAMETER(2, "非法参数"),

    WRONG_TYPE(1001,"折扣计算服务类型错误"),
    UNKNOWN_ERROR(1002, "商品无拼团营销配置"),
    FULL_TEAM(1003, "拼团已满"),
    NO_EFFECTIVE(1004,"活动未生效"),
    NOT_IN_VALID_DATE(1005,"非可参与时间范围"),
    LIMIT_ENABLED(1006,"用户参与次数校验，已达可参与上限"),
    UPDATE_ORDER_LIST_STATUS_FAILED(1007, "更新未支付订单状态失败"),
    UPDATE_COMPLETED_ORDER_STATUS_FAILED(1008, "更新拼团达成数量失败"),
    UPDATE_ORDER_STATUS_FAILED(1009, "更新组队状态至成功失败"),
    WRONG_TIME_FOR_OUT_TRADE(1010, "订单交易时间不在拼团有效时间范围内"),
    NOTIFY_API_ERROR(1011, "拼团回调 HTTP 接口服务异常"),
    ;

    private Integer code;
    private String info;

}
