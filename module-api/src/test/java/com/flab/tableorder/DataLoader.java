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
	public static Map<String, Object> getResponseData(TestRestTemplate restTemplate, String url, HttpMethod httpMethod, HttpEntity httpEntity) {
		ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
					url,
					httpMethod,
					httpEntity,
					new ParameterizedTypeReference<Map<String, Object>>() {}
				);
		return responseEntity.getBody();
	}

	public static Store getStoreInfo(ObjectMapper objectMapper) {
		InputStream inputStream = DataLoader.class.getResourceAsStream("/store_1.json");

		Store store = null;
		try {
			StoreDTO storeDTO = objectMapper.readValue(inputStream, new TypeReference<>() {});
			store = StoreMapper.INSTANCE.toEntity(storeDTO);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return store;
	}
}
