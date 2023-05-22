package com.hanghae7.alcoholcommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * EnableFeignClients는 FeignClient를 사용하기위해 사용
 */
@EnableFeignClients
@SpringBootApplication
public class AlcoholcommunityApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AlcoholcommunityApplication.class, args);
	}

}
