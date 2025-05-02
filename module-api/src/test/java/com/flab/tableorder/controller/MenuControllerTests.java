package com.flab.tableorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTests {
	@Autowired
	private MockMvc mockMvc;
//
//	@Test
//	void getMenuSuccess() throws Exception {
//		mockMvc.perform(get("/menu"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code").value(200))
////			.andExpect(jsonPath("$.data[0].menu[1].price").value(not(0)))
//		;
//	}
//
//	@Test
//	void getMenuDetailSuccess() throws Exception {
//		long menuId = 2;
//		mockMvc.perform(get("/menu/" + menuId))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code").value(200))
//			.andExpect(jsonPath("$.data.menuId").value(menuId))
//			.andExpect(jsonPath("$.data.price", greaterThan(0)))
//		;
//	}
//
//	@Test
//	void getCallSuccess() throws Exception {
//		mockMvc.perform(get("/menu/call"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.code").value(200))
////			.andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
//		;
//	}

}
