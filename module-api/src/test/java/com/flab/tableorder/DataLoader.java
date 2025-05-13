package com.flab.tableorder;

import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.OptionCategory;
import com.flab.tableorder.domain.Store;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    public static List getListFromStream(String dir, String fileName, Class cls) {
        InputStream inputStream = DataLoader.class.getResourceAsStream("/" + dir + "/" + fileName);

        List list = null;
        try {
            list = objectMapper.readValue(inputStream, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, cls)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static List<Category> getCategoryList(String fileName) {
        return getListFromStream("category", fileName, Category.class);
    }

    public static List<Menu> getMenuList(String fileName) {
        return getListFromStream("menu", fileName, Menu.class);
    }

    public static List<OptionCategory> getOptionList(String fileName) {
        return getListFromStream("option", fileName, OptionCategory.class);
    }
}
