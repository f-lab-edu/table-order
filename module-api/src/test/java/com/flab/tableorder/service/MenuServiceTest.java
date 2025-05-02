package com.flab.tableorder.service;

import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;
import com.flab.tableorder.mapper.*;

import java.io.*;
import java.util.*;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	private StoreRepository storeRepository;
	@Mock
	private MenuRepository menuRepository;
	@InjectMocks
	private MenuService menuService;

	@Test
	void getAllMenu_NotFound() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			menuService.getAllMenu();
		});
	}

	@Test
	void getAllMenu_Success() throws IOException {
		Store mockStore = getStoreInfo();
		Long storeId = mockStore.getStoreId();
		StoreContext.setStoreId(storeId);

		when(storeRepository.findByStoreId(storeId)).thenReturn(Optional.of(mockStore));

		List<MenuCategoryDTO> menu = menuService.getAllMenu();

		assertThat(menu).isNotNull();
		assertThat(menu.size()).isSameAs(mockStore.getCategories().size());
	}

	public Store getStoreInfo() throws IOException {
		InputStream inputStream = getClass().getResourceAsStream("/store_1.json");

		StoreDTO storeDTO = objectMapper.readValue(inputStream, new TypeReference<>() {});
		Store store = StoreMapper.INSTANCE.toEntity(storeDTO);

		return store;
	}

}
