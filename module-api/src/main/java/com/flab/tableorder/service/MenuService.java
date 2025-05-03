package com.flab.tableorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;
import com.flab.tableorder.mapper.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Service
@Slf4j @RequiredArgsConstructor
public class MenuService {
	private final ObjectMapper objectMapper;
	private final MenuCategoryRepository menuCategoryRepository;
	private final MenuRepository menuRepository;

	@Transactional(readOnly = true)
	public List<MenuCategoryDTO> getAllMenu() {
		List<MenuCategory> categories = menuCategoryRepository.findAllByStore_StoreId(StoreContext.getStoreId());
		if (categories == null || categories.isEmpty()) throw new EntityNotFoundException("Store not found");

		return MenuMapper.INSTANCE.toDTO(categories);
	}

	@Transactional(readOnly = true)
	public MenuDTO getMenu(Long menuId) {
		Menu menu = menuRepository.findByMenuIdAndStore_StoreId(StoreContext.getStoreId(), menuId)
				.orElseThrow(() -> new EntityNotFoundException("Menu not found"));

		return MenuMapper.INSTANCE.toDTO(menu);
	}
}
