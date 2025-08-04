package com.gzc.api;

import com.gzc.api.dto.req.MarketIndexShowRequestDTO;
import com.gzc.api.dto.resp.MarketIndexShowResponseDTO;
import com.gzc.api.response.Response;

public interface IMarketIndexShowService {
    /**
     * 查询拼团营销配置
     *
     * @param marketIndexShowRequestDTO 营销商品信息
     * @return 营销配置信息
     */
    Response<MarketIndexShowResponseDTO> queryGroupBuyMarketConfig(MarketIndexShowRequestDTO marketIndexShowRequestDTO);
}