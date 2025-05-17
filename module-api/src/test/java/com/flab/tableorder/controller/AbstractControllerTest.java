package com.flab.tableorder.controller;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Call;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.StoreDTO;
import com.flab.tableorder.mapper.CategoryMapper;
import com.flab.tableorder.mapper.MenuMapper;
import com.flab.tableorder.mapper.StoreMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractControllerTest {
    private static boolean initialized = false;

    @LocalServerPort
    private int port;

    protected String url;
    protected HttpHeaders headers;

    protected StoreDTO storeDTO;
    protected Store store;
    protected List<Category> categoryList;
    protected List<Menu> menuList;
    protected List<Option> optionList;
    protected List<Call> callList;

    @BeforeAll
    void init() {
        if (initialized) return;

        this.initialized = true;
        this.url = "http://localhost:" + port;
        String fileName = "pizza.json";
        this.store = DataLoader.getDataInfo("store", fileName, Store.class);
        this.storeDTO = StoreMapper.INSTANCE.toDTO(store);

        if (store != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + store.getApiKey());
            this.headers = headers;
        }

        this.categoryList = DataLoader.getDataList("category", fileName, Category.class);
        this.menuList = DataLoader.getDataList("menu", fileName, Menu.class);
        this.optionList = DataLoader.getDataList("option", fileName, Option.class);
        this.callList = DataLoader.getDataList("call", fileName, Call.class);

        Map<String, List<Menu>> menuListMap = menuList.isEmpty()
            ? new HashMap<>()
            : menuList.stream()
                .collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

        List<MenuCategoryDTO> menuCategoryDTOList = CategoryMapper.INSTANCE.toDTO(categoryList.stream()
            .filter(category -> !category.isOption())
            .toList());

        for (MenuCategoryDTO menuCategoryDTO : menuCategoryDTOList) {
            List<Menu> menuEntity = menuListMap.get(menuCategoryDTO.getCategoryId());
            List<MenuDTO> menu = menuEntity == null ? List.of() : MenuMapper.INSTANCE.toDTO(menuEntity);
            menuCategoryDTO.setMenu(menu);
        }

        this.storeDTO.setCategories(menuCategoryDTOList);
    }
}
