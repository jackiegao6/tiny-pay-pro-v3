package com.gzc.domain.trade.model.valobj;


import lombok.*;

import java.util.Date;

/**
 * @description 拼团进度值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyProgressVO {

    /** 目标数量 */
    private Integer targetCount;
    /** 完成数量 */
    private Integer completeCount;
    /** 锁单数量 */
    private Integer lockCount;

    /** 拼单组队ID */
    private String teamId;
    /** 活动ID */
    private Long activityId;
    /** 状态（0-拼单中、1-完成、2-失败） */
    private TradeOrderStatusEnumVO status;

    private Date validEndTime;
    private Date outTradeTime;

    private NotifyConfigVO notifyConfigVO;


}
