package com.gzc.domain.trade.service.settlement;

import com.gzc.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicContext {

    private GroupBuyProgressVO groupBuyProgressVO;
    private Date outTradeTime;

}
