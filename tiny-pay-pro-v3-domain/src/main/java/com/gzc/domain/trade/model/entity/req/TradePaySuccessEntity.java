package com.gzc.domain.trade.model.entity.req;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradePaySuccessEntity {


    private String userId;
    private String teamId;
    private String outTradeNo;
}
