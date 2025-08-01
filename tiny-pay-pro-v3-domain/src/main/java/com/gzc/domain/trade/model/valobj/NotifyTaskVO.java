package com.gzc.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyTaskVO {

    /**
     * 拼单组队ID
     */
    private String teamId;
    /**
     * 回调接口
     */
    private String notifyUrl;
    /**
     * 回调次数
     */
    private Integer notifyCount;
    /**
     * 参数对象
     */
    private String parameterJson;

    public String lockKey() {
        return "notify_job_lock_key:" + this.teamId;
    }

}
