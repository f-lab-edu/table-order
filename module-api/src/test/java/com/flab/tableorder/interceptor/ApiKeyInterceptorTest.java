package com.flab.tableorder.interceptor;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.domain.StoreRepository;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiKeyInterceptorTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StoreRepository storeRepository;

    private String url;
    private String storeId = "";
    private String apiKey = "testAPI";

    @BeforeAll
    void init() {
        this.url = "http://localhost:" + port + "/menu";

        Store store = new Store();
        store.setStoreId(new ObjectId(this.storeId));
        store.setApiKey(this.apiKey);

        storeRepository.save(store);
    }

    @Test
    void APIKey_NoAuthorization() {
        Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, null);
        assertThat(responseData.get("code")).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void APIKey_NotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer notfound");

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, httpEntity);
        assertThat(responseData.get("code")).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(responseData.get("message")).isEqualTo("Api Key not found");
    }

    @Test
    void APIKeyCache_Success() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.apiKey);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, httpEntity);
        assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());

        responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, httpEntity);
        assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());
    }
}
