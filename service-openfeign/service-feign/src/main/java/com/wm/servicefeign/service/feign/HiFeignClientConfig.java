package com.wm.servicefeign.service.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author wangmin
 * @create 2023-11-29 10:54
 */
@Slf4j
@Configuration
public class HiFeignClientConfig {

    @Bean
    public Client client(){
        return new MyAroundClient();
    }


    //前置后置处理Feign调用
    private static class MyAroundClient implements Client{
        //
        private final Client client = new Client.Default(null, null);

        @Override
        public Response execute(Request request, Request.Options options) throws IOException {
            Response response = client.execute(request, options);
            log.info("进入feign自定义client");
            if (response.status() == 500) {
                return Response.builder()
                        .status(200)
                        .reason(response.reason())
                        .headers(response.headers())
                        .body(response.body())
                        .request(response.request())
                        .build();
            }
            return response;
        }
    }
}
