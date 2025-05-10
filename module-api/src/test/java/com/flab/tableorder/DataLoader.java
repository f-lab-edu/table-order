package com.flab.tableorder;

import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;
import com.flab.tableorder.mapper.*;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class DataLoader {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static Map<String, Object> getResponseData(TestRestTemplate restTemplate, String url, HttpMethod httpMethod, HttpEntity httpEntity) {
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
					url,
					httpMethod,
					httpEntity,
					new ParameterizedTypeReference<Map<String, Object>>() {}
				);
		return responseEntity.getBody();
	}

	public static Store getStoreInfo(String fileName) {
		InputStream inputStream = DataLoader.class.getResourceAsStream("/store/" + fileName);

		Store store = null;
		try {
			store = objectMapper.readValue(inputStream, Store.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return store;
	}

	public static List<Category> getCategoryList(String fileName) {
		InputStream inputStream = DataLoader.class.getResourceAsStream("/category/" + fileName);

		List<Category> categoryList = null;
		try {
			categoryList = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return categoryList;
	}

	public static List<Menu> getMenuList(String fileName) {
		InputStream inputStream = DataLoader.class.getResourceAsStream("/menu/" + fileName);

		List<Menu> menuList = null;
		try {
			menuList = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, Menu.class));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return menuList;
	}
}
