package com.flab.tableorder.config;

import com.flab.tableorder.interceptor.ApiKeyInterceptor;
import com.flab.tableorder.interceptor.ResquestInterceptor;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	private final ApiKeyInterceptor apiKeyInterceptor;
	private final ResquestInterceptor resquestInterceptor;


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(resquestInterceptor)
				.addPathPatterns("/**");

		registry.addInterceptor(apiKeyInterceptor)
				.addPathPatterns("/**");
	}
}
