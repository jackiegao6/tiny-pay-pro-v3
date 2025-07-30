package com.gzc.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),

    WRONG_TYPE("S1001","折扣计算服务类型错误"),
    UNKNOWN_ERROR("S1002", "商品无拼团营销配置"),
    ;

    private String code;
    private String info;

}
