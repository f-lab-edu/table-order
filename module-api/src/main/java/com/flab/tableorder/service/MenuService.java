package com.flab.tableorder.service;

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

	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	@Transactional(readOnly = true)
	public List<MenuCategoryDTO> getAllMenu() {
		Store store = storeRepository.findByStoreId(StoreContext.getStoreId())
				.orElseThrow(() -> new EntityNotFoundException("Store not found"));

		List<MenuCategory> categories = store.getCategories();

		return MenuMapper.INSTANCE.toDTO(categories);
	}

}
