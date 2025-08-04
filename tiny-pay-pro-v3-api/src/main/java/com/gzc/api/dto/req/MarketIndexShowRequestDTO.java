package com.gzc.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketIndexShowRequestDTO {

    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String goodsId;
}
