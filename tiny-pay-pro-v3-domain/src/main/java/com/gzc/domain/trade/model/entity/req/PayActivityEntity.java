package com.gzc.domain.trade.model.entity.req;
import com.gzc.domain.trade.model.valobj.NotifyConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description 拼团，支付活动实体对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {

    /** 拼单组队ID */
    private String teamId;
    /** 活动ID */
    private Long activityId;
    /** 拼团开始时间 */
    private Date startTime;
    /** 拼团结束时间 */
    private Date endTime;

    private Integer validTime;
    /** 目标数量 */
    private Integer targetCount;

    private NotifyConfigVO notifyConfigVO;

}
