package com.flab.tableorder.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.domain.StoreRepository;
import com.flab.tableorder.dto.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiKeyInterceptorTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private StoreRepository storeRepository;

	private String url;
	private Long storeId = 1L;
	private String apiKey = "testAPI";

	@BeforeAll
	void init() {
		this.url = "http://localhost:" + port;

		Store store = new Store();
		store.setStoreId(this.storeId);
		store.setApiKey(this.apiKey);

		storeRepository.save(store);
	}


	private ResponseDTO getResponseData(HttpEntity httpEntity) {
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					this.url + "/menu",
					HttpMethod.GET,
					httpEntity,
					new ParameterizedTypeReference<ResponseDTO>() {}
				);
		return responseEntity.getBody();
	}

	@Test
	void APIKey_NoAuthorization() {
		assertThat(getResponseData(null).getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	void APIKey_NotFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer notfound");

		HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

		ResponseDTO responseBody = getResponseData(httpEntity);
		assertThat(responseBody.getCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertThat(responseBody.getMessage()).isEqualTo("Api Key not found");
	}

	@Test
	void APIKeyCache_Success() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + this.apiKey);

		HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

		ResponseDTO responseBody = getResponseData(httpEntity);
		assertThat(responseBody.getCode()).isEqualTo(HttpStatus.OK.value());

		responseBody = getResponseData(httpEntity);
		assertThat(responseBody.getCode()).isEqualTo(HttpStatus.OK.value());
	}
}
