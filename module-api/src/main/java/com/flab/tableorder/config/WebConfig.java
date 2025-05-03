package com.flab.tableorder.config;

import com.flab.tableorder.interceptor.*;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

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
