package com.flab.tableorder.interceptor;

import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;

import java.io.*;
import java.util.*;

import com.flab.tableorder.service.StoreService;
import jakarta.servlet.http.*;
import lombok.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.servlet.*;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {
	private final ObjectMapper objectMapper;
	private final StoreService storeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		String apiKeyHeader = "Bearer ";
		int httpStatus;

		String apiKey = request.getHeader("Authorization");
		if (apiKey == null || !apiKey.startsWith(apiKeyHeader)) {
			httpStatus = HttpStatus.UNAUTHORIZED.value();
			ResponseDTO responseData = new ResponseDTO<>(httpStatus, "Invalid API Key");

			response.setStatus(httpStatus);
			response.setContentType("application/json");
			response.getWriter().write(objectMapper.writeValueAsString(responseData));

			return false;
		}

		apiKey = apiKey.substring(apiKeyHeader.length());
		String storeId = storeService.getStoreIdByApiKey(apiKey);

		StoreContext.setStoreId(storeId);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		StoreContext.clear();
	}
}
