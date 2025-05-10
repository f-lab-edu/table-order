package com.flab.tableorder.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.dto.ResponseDTO;
import com.flab.tableorder.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResquestInterceptor implements HandlerInterceptor {
	private final ObjectMapper objectMapper;
	private final StoreService storeService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;

		String method = req.getMethod();
		String uri = req.getRequestURI();
		String query = req.getQueryString();

		log.info("HTTP Request - Method: {}, Path: {}, Query: {}", method, uri, query != null ? query : "");

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		StoreContext.clear();
	}
}
