package com.gzc.infrastructure.gateway;

import com.gzc.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;


@Slf4j
@Service
@RequiredArgsConstructor
public class GroupBuyNotifyService {

    private final OkHttpClient okHttpClient;

    @Value("${app.config.pay-mall.team-finish-notify-url}")
    private String teamFinishNotifyUrl;

    public String settlementFinishNotify(String apiUrl, String notifyRequestDTOJSON) throws Exception {
        try {
            // 1. 构建参数
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, notifyRequestDTOJSON);
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

            // 2. 调用接口
            Response response = okHttpClient.newCall(request).execute();

            // 3. 返回结果
            return response.body().string();
        } catch (Exception e) {
            log.error("拼团回调 HTTP 接口服务异常 {}", apiUrl, e);
            throw new AppException(ResponseCode.NOTIFY_API_ERROR.getCode(), ResponseCode.NOTIFY_API_ERROR.getInfo());
        }
    }

    public String teamFinishNotify(String notifyRequestDTOJSON){
        try{
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, notifyRequestDTOJSON);

            Request request = new Request.Builder()
                    .url(teamFinishNotifyUrl)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            log.error("拼团回调 组队完成 HTTP 接口服务异常", e);
            throw new AppException(ResponseCode.NOTIFY_API_ERROR.getCode(), ResponseCode.NOTIFY_API_ERROR.getInfo());
        }
    }
}
