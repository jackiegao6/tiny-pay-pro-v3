package com.gzc.domain.trade.adapter.port;

import com.gzc.domain.trade.model.valobj.NotifyTaskVO;

/**
 * @description 交易服务接口
 */
public interface ITradePort {

    String settlementFinishNotify(NotifyTaskVO notifyTaskVO) throws Exception;

}
