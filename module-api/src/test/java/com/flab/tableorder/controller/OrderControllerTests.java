package com.flab.tableorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTests {
    @Autowired
    private MockMvc mockMvc;

    // @Test
    // void postOrderSuccess() throws Exception {
    // Map<String, Object> option1 = new HashMap<>();
    // option1.put("optionId", 1);
    // option1.put("quantity", 1);
    // option1.put("price", 1000);
    //
    // Map<String, Object> option2 = new HashMap<>();
    // option2.put("optionId", 2);
    // option2.put("quantity", 2);
    // option2.put("price", 1000);
    //
    // Map<String, Object> option3 = new HashMap<>();
    // option3.put("optionId", 4);
    // option3.put("quantity", 1);
    // option3.put("price", 4000);
    //
    //
    // Map<String, Object> menu = new HashMap<>();
    // menu.put("menuId", 2);
    // menu.put("quantity", 1);
    // menu.put("price", 8500);
    // menu.put("options", List.of(option1, option2, option3));
    //
    // Map<String, Object> requestData = new HashMap<>();
    //
    //
    // mockMvc.perform(post("/order/table/1")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(requestData.toString())
    // )
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.code").value(200))
    // ;
    // }
    //
    // @Test
    // void postCallSuccess() throws Exception {
    // String jsonRequest = "{\"call\": [1, 3]}";
    //
    // mockMvc.perform(post("/order/table/1/call")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content(jsonRequest)
    // )
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$.code").value(200))
    // ;
    // }
}
