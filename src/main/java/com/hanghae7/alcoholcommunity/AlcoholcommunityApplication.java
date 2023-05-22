package com.hanghae7.alcoholcommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * EnableFeignClients는 FeignClient를 사용하기위해 사용
 */
@EnableFeignClients
@SpringBootApplication
public class AlcoholcommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlcoholcommunityApplication.class, args);
	}

}
