package com.gzc.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockMarketPayOrderRequestDTO {

    private String userId;
    private String teamId;
    private Long activityId;
    private String goodsId;
    private String outTradeNo;
    private NotifyConfigVO notifyConfigVO;

    public void setNotifyUrl(String notifyUrl){
        NotifyConfigVO notifyConfigVO = new NotifyConfigVO();
        notifyConfigVO.setNotifyType("HTTP");
        notifyConfigVO.setNotifyUrl(notifyUrl);
        this.notifyConfigVO = notifyConfigVO;
    }

    @Data
    public static class NotifyConfigVO{
        private String notifyType;
        private String notifyMQ;
        private String notifyUrl;
    }

}
