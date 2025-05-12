package com.flab.tableorder.controller;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.OptionCategory;
import com.flab.tableorder.domain.OptionRepository;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.domain.StoreRepository;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.StoreDTO;
import com.flab.tableorder.mapper.CategoryMapper;
import com.flab.tableorder.mapper.MenuMapper;
import com.flab.tableorder.mapper.StoreMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

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
    Environment env;

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OptionRepository optionRepository;

    private StoreDTO storeDTO;
    private String url;
    private HttpEntity<Void> httpEntity;

    private Store store;
    private List<Category> categoryList;
    private List<Menu> menuList;
    private List<OptionCategory> optionCategoryList;

    @BeforeAll
    void init() {
        this.url = "http://localhost:" + port + "/menu";
        this.store = DataLoader.getStoreInfo("pizza.json");
        this.storeDTO = StoreMapper.INSTANCE.toDTO(store);

        if (store != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + store.getApiKey());
            this.httpEntity = new HttpEntity<>(headers);
        }

        this.categoryList = DataLoader.getCategoryList("pizza.json");
        this.menuList = DataLoader.getMenuList("pizza.json");
        this.optionCategoryList = DataLoader.getOptionList("pizza.json");

        Map<String, List<Menu>> menuListMap = menuList.isEmpty()
            ? new HashMap<>()
            : menuList.stream()
                .collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

        List<MenuCategoryDTO> menuCategoryDTOList = CategoryMapper.INSTANCE.toDTO(categoryList);

        for (MenuCategoryDTO menuCategoryDTO : menuCategoryDTOList) {
            List<Menu> menuEntity = menuListMap.get(menuCategoryDTO.getCategoryId());
            List<MenuDTO> menu = menuEntity == null ? new ArrayList<>() : MenuMapper.INSTANCE.toDTO(menuEntity);
            menuCategoryDTO.setMenu(menu);
        }

        this.storeDTO.setCategories(menuCategoryDTOList);
    }

    @Test
    public void saveData() {
        if (this.store != null) storeRepository.save(store);
        if (this.categoryList != null) categoryRepository.saveAll(categoryList);
        if (this.menuList != null) menuRepository.saveAll(menuList);
        if (this.optionCategoryList != null) optionRepository.saveAll(optionCategoryList);
    }

    @Test
    void getAllMenu_Success() {
        Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url, HttpMethod.GET, this.httpEntity);

        assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());

        List<MenuCategoryDTO> storeMenuCategoryList = this.storeDTO.getCategories();
        List<Map<String, Object>> resMenuCategoryList = (List<Map<String, Object>>) responseData.get("data");

        assertThat(resMenuCategoryList.size()).isEqualTo(storeMenuCategoryList.size());

        for (int i = 0; i < storeMenuCategoryList.size(); i++) {
            MenuCategoryDTO storeMenuCategory = storeMenuCategoryList.get(i);
            Map<String, Object> resMenuCategory = resMenuCategoryList.get(i);

            assertThat(resMenuCategory.get("categoryId").toString()).isEqualTo(storeMenuCategory.getCategoryId());
            assertThat(resMenuCategory.get("categoryName")).isEqualTo(storeMenuCategory.getCategoryName());

            List<MenuDTO> storeMenuList = storeMenuCategory.getMenu();
            List<Map<String, Object>> resMenuList = (List<Map<String, Object>>) resMenuCategory.get("menu");

            assertThat(resMenuList.size()).isEqualTo(storeMenuList.size());

            for (int j = 0; j < storeMenuList.size(); j++) {
                MenuDTO menu = storeMenuList.get(j);
                Map<String, Object> resMenu = resMenuList.get(j);

                assertThat(resMenu.get("menuId").toString()).isEqualTo(menu.getMenuId());
                assertThat(resMenu.get("menuName")).isEqualTo(menu.getMenuName());

                int price = Integer.parseInt(resMenu.get("price").toString());
                int salePrice = Integer.parseInt(resMenu.get("salePrice").toString());

                assertThat(price).isGreaterThan(0);
                assertThat(price).isGreaterThan(salePrice);
            }
        }

    }

     @Test
     void getMenu_Success() throws Exception {
         Menu storeMenu = this.menuList.get(0);
         String menuId = storeMenu.getMenuId().toString();

         Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url + "/" + menuId, HttpMethod.GET, this.httpEntity);

         assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());

         Map<String, Object> resMenu = (Map<String, Object>) responseData.get("data");

         assertThat(resMenu.get("menuId")).isEqualTo(menuId);
         assertThat(resMenu.get("menuName")).isEqualTo(storeMenu.getMenuName());
     }

    // @Test
    // void getCallSuccess() throws Exception {
    // mockMvc.perform(get("/menu/call"))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.code").value(200))
    //// .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
    // ;
    // }

}
