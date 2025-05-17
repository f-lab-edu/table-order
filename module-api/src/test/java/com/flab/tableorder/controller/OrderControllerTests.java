package com.flab.tableorder.controller;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.dto.OptionDTO;
import com.flab.tableorder.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTests extends AbstractControllerTest{
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    @Override
    void init() {
        super.init();
    }

    @Test
    void postOrderSuccess() {
        Menu menu = this.menuList.get(0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMenuId(menu.getMenuId().toString());
        orderDTO.setQuantity(1);
        orderDTO.setPrice(menu.getPrice());
        orderDTO.setOptions(this.optionList.stream()
            .filter(opt -> menu.getOptionCategoryIds().contains(opt.getCategoryId()))
            .map(opt -> {
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setOptionId(opt.getOptionId().toString());
                optionDTO.setPrice(opt.getPrice());
                optionDTO.setQuantity(1);

                return optionDTO;
            })
            .toList()
        );
        HttpEntity httpEntity = new HttpEntity(List.of(orderDTO), headers);

        Map<String, Object> responseData = DataLoader.getResponseData(restTemplate, this.url + "/order/table/1", HttpMethod.POST, httpEntity);
        assertThat(responseData.get("code")).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void postCallSuccess() {
    }
}
