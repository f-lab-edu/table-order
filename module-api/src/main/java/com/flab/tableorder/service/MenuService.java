package com.flab.tableorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.mapper.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@Slf4j @RequiredArgsConstructor
public class MenuService {
	private final ObjectMapper objectMapper;
	private final CategoryRepository categoryRepository;
	private final MenuRepository menuRepository;

	@Transactional(readOnly = true)
	public List<MenuCategoryDTO> getAllMenu(String storeId) {
		List<Category> categoryList = categoryRepository.findAllByStoreId(storeId);
		if (categoryList == null || categoryList.isEmpty()) return new ArrayList<>();

		List<ObjectId> categoryIds = categoryList.stream()
				.map(Category::getCategoryId)
				.collect(Collectors.toList());

		List<Menu> menuList = menuRepository.findAllByCategoryIdIn(categoryIds);

		Map<String, List<Menu>> menuListMap = menuList == null
				? new HashMap<>()
				: menuList.stream()
					.collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

		List<MenuCategoryDTO> result = CategoryMapper.INSTANCE.toDTO(categoryList);

		for (MenuCategoryDTO menuCategory: result) {
			List<MenuDTO> menu = MenuMapper.INSTANCE.toDTO(menuListMap.get(menuCategory.getCategoryId()));
			menu = menu == null ? new ArrayList<>() : menu;
			menuCategory.setMenu(menu);
		}

		return result;
	}

	@Transactional(readOnly = true)
	public MenuDTO getMenu(String storeId, String menuId) {
		Menu menu = menuRepository.findByMenuIdAndStore_StoreId(storeId, menuId)
				.orElseThrow(() -> new EntityNotFoundException("Menu not found"));

		return MenuMapper.INSTANCE.toDTO(menu);
	}
}
