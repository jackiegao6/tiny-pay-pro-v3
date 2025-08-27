package com.gzc.domain.trade.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum NotifyConfigEnumVO {

    HTTP("HTTP","HTTP 回调"),
    MQ("MQ","MQ 回调"),
    ;

    private String code;
    private String info;
}
