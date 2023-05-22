package com.hanghae7.alcoholcommunity.domain.sociallogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;

/**
 * 로드밸런싱 에러가 안나도록 해주는 부분이라고 함.
 */
@Configuration
public class FeignConfiguration {

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }
}
