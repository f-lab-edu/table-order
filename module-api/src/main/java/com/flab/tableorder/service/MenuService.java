package com.flab.tableorder.service;

import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.mapper.CategoryMapper;
import com.flab.tableorder.mapper.MenuMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<MenuCategoryDTO> getAllMenu(String storeId) {
        List<Category> categoryList = categoryRepository.findAllByStoreId(storeId);
        if (categoryList.isEmpty()) return new ArrayList<>();

        List<ObjectId> categoryIds = categoryList.stream()
            .map(category -> category.getCategoryId())
            .collect(Collectors.toList());

        List<Menu> menuList = menuRepository.findAllByCategoryIdIn(categoryIds);

        Map<String, List<Menu>> menuListMap = menuList.isEmpty()
            ? new HashMap<>()
            : menuList.stream()
                .collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

        List<MenuCategoryDTO> result = CategoryMapper.INSTANCE.toDTO(categoryList);

        for (MenuCategoryDTO menuCategory : result) {
            List<Menu> menuEntity = menuListMap.get(menuCategory.getCategoryId());
            List<MenuDTO> menu = menuEntity == null ? new ArrayList<>() : MenuMapper.INSTANCE.toDTO(menuEntity);
            menuCategory.setMenu(menu);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public MenuDTO getMenu(String storeId, String menuId) {
        return menuRepository.findByMenuIdAndStore_StoreId(storeId, menuId)
            .map(menu -> MenuMapper.INSTANCE.toDTO(menu))
            .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    }
}
