package com.flab.tableorder;

import com.fasterxml.jackson.databind.JavaType;
import com.flab.tableorder.domain.Call;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.OptionCategory;
import com.flab.tableorder.domain.Store;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    private static <T> T getDataFromStream(String dir, String fileName, JavaType javaType) {
        String separator = "/";
        StringJoiner stringJoiner = new StringJoiner(separator, separator, "")
            .add(dir)
            .add(fileName);

        InputStream inputStream = DataLoader.class.getResourceAsStream(stringJoiner.toString());

        T data = null;
        try {
            data = objectMapper.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public static <T> T getDataInfo(String dir, String fileName, Class cls) {
        return getDataFromStream(dir, fileName, objectMapper.constructType(cls));
    }

    public static List getDataList(String dir, String fileName, Class cls) {
        return getDataFromStream(dir, fileName, objectMapper.getTypeFactory()
            .constructCollectionType(List.class, cls));
    }
}
