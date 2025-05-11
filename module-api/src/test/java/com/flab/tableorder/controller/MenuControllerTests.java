package com.flab.tableorder.controller;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.domain.StoreRepository;
import com.flab.tableorder.mapper.StoreMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuControllerTests {
	@LocalServerPort
    private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private StoreRepository storeRepository;

	private String url;
	private String apiKey;
	private Store store;
	private HttpEntity<Void> httpEntity;

	@BeforeAll
	void init() {
		this.url = "http://localhost:" + port + "/menu";
		this.store = DataLoader.getStoreInfo("pizza.json");
		if (store != null) {
			storeRepository.save(store);
			this.apiKey = store.getApiKey();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + apiKey);
			this.httpEntity = new HttpEntity<>(headers);
		}

		try {
			log.info(objectMapper.writeValueAsString(StoreMapper.INSTANCE.toDTO(store)));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testStartServer() {
	   	System.out.println("Start Test Server : " + this.url);
	}

//	@Test
//	void getMenu_Success() throws Exception {
//		Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, this.httpEntity);
//
//		assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());
//
//		List<MenuCategoryDTO> storeMenuCategoryList = MenuMapper.INSTANCE.toDTO(this.store.getCategories());
//		List<Map<String, Object>> resMenuCategoryList = (List<Map<String, Object>>) responseData.get("data");
//
//		assertThat(resMenuCategoryList.size()).isEqualTo(storeMenuCategoryList.size());
//
//		for (int i = 0; i < storeMenuCategoryList.size(); i++) {
//			MenuCategoryDTO storeMenuCategory = storeMenuCategoryList.get(i);
//			Map<String, Object> resMenuCategory = resMenuCategoryList.get(i);
//
//			assertThat(Long.parseLong(resMenuCategory.get("categoryId").toString())).isEqualTo(storeMenuCategory.getCategoryId());
//			assertThat(resMenuCategory.get("categoryName")).isEqualTo(storeMenuCategory.getCategoryName());
//
//			List<MenuDTO> storeMenuList = storeMenuCategory.getMenu();
//			List<Map<String, Object>> resMenuList = (List<Map<String, Object>>) resMenuCategory.get("menu");
//
//			assertThat(resMenuList.size()).isEqualTo(storeMenuList.size());
//
//			for (int j = 0; j < storeMenuList.size(); j++) {
//				MenuDTO menu = storeMenuList.get(j);
//				Map<String, Object> resMenu = resMenuList.get(j);
//
//				assertThat(Long.parseLong(resMenu.get("menuId").toString())).isEqualTo(menu.getMenuId());
//				assertThat(resMenu.get("menuName")).isEqualTo(menu.getMenuName());
//			}
//		}
//	}

//	@Test
//	void getMenuDetailSuccess() throws Exception {
//		Menu storeMenu = store.getCategories().get(0).getMenu().get(0);
//		Long menuId = storeMenu.getMenuId();
//		Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url + "/" + menuId, HttpMethod.GET, this.httpEntity);
//
//		assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());
//
//		Map<String, Object> resMenu = (Map<String, Object>) responseData.get("data");
//		assertThat(Long.parseLong(resMenu.get("menuId").toString())).isEqualTo(menuId);
//		assertThat(resMenu.get("menuName")).isEqualTo(storeMenu.getMenuName());
//	}

//	@Test
//	void getCallSuccess() throws Exception {
//		mockMvc.perform(get("/menu/call"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code").value(200))
////			.andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
//		;
//	}

}
