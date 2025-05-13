package com.flab.tableorder.service;

import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.StoreNotFoundException;
import com.flab.tableorder.mapper.CategoryMapper;
import com.flab.tableorder.mapper.MenuMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Category> categoryList = categoryRepository.findAllByStoreId(new ObjectId(storeId));
        if (categoryList.isEmpty()) return new ArrayList<>();

        List<ObjectId> categoryIds = categoryList.stream()
            .map(category -> category.getCategoryId())
            .collect(Collectors.toList());

        List<Menu> menuList = menuRepository.findAllByCategoryIdIn(categoryIds);
        Map<String, List<Menu>> menuListMap = menuList.isEmpty()
            ? new HashMap<>()
            : menuList.stream()
                .collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

        return CategoryMapper.INSTANCE.toDTO(categoryList).stream()
            .map(menuCategoryDTO -> {
                List<MenuDTO> menus = Optional.ofNullable(menuListMap.get(menuCategoryDTO.getCategoryId()))
                    .map(menu -> MenuMapper.INSTANCE.toDTO(menu))
                    .orElseGet(() -> new ArrayList());

                menuCategoryDTO.setMenu(menus);
                return menuCategoryDTO;
            })
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuDTO getMenu(String storeId, String menuId) {
        ObjectId objMenuId = new ObjectId(menuId);
        Menu menu = menuRepository.findByMenuId(objMenuId)
            .orElseThrow(() -> new MenuNotFoundException("Menu not found for menuId: " + menuId));

        ObjectId findCategoryId = menu.getCategoryId();
        String findStoreId = categoryRepository.findByCategoryId(findCategoryId)
            .map(category -> category.getStoreId().toString())
            .orElseThrow(() -> new StoreNotFoundException("Store not found for categoryId: " + findCategoryId))
        ;

        if (!findStoreId.equals(storeId)) throw new StoreNotFoundException("Store mismatch: expected " + findStoreId + ", but got " + storeId);

        return MenuMapper.INSTANCE.toDTO(menu);
    }
}
