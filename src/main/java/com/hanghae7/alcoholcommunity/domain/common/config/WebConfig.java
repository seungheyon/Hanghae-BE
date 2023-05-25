package com.hanghae7.alcoholcommunity.domain.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Please explain the class!!
 *
 * @fileName      : WebConfig
 * @author        : mycom
 * @since         : 2023-05-25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new TimingInterceptor()).addPathPatterns("/**");
	}
}