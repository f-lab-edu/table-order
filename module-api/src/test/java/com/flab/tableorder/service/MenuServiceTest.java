package com.flab.tableorder.service;

import com.flab.tableorder.DataLoader;
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
	@Mock
	private StoreRepository storeRepository;
	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private MenuService menuService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Test
	void getAllMenu_NotFound() {
		StoreContext.setStoreId(0L);
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			menuService.getAllMenu();
		});
	}

	@Test
	void getAllMenu_Success() throws IOException {
		Store mockStore = DataLoader.getStoreInfo(objectMapper);
		Long storeId = mockStore.getStoreId();
		StoreContext.setStoreId(storeId);

		when(storeRepository.findByStoreId(storeId)).thenReturn(Optional.of(mockStore));

		List<MenuCategoryDTO> menu = menuService.getAllMenu();

		assertThat(menu).isNotNull();
		assertThat(menu).isEqualTo(MenuMapper.INSTANCE.toDTO(mockStore.getCategories()));
	}

	@Test
	void getMenu_NotFound() throws IOException {
		StoreContext.setStoreId(0L);
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			menuService.getMenu(0L);
		});
	}

	@Test
	void getMenu_Success() throws IOException {
		Store mockStore = DataLoader.getStoreInfo(objectMapper);
		Long storeId = mockStore.getStoreId();
		StoreContext.setStoreId(storeId);

		Menu mockMenu = mockStore.getCategories().get(0).getMenu().get(0);
		Long menuId = mockMenu.getMenuId();

		when(menuRepository.findByMenuIdAndStore_StoreId(storeId, menuId)).thenReturn(Optional.of(mockMenu));

		MenuDTO menu = menuService.getMenu(menuId);

		assertThat(menu).isNotNull();
		assertThat(menu).isEqualTo(MenuMapper.INSTANCE.toDTO(mockMenu));
	}

}
