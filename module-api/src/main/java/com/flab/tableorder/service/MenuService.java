package com.flab.tableorder.service;

import com.flab.tableorder.domain.CallRepository;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Option;
import com.flab.tableorder.domain.OptionRepository;
import com.flab.tableorder.dto.CallDTO;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.OptionDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.StoreNotFoundException;
import com.flab.tableorder.mapper.CallMapper;
import com.flab.tableorder.mapper.CategoryMapper;
import com.flab.tableorder.mapper.MenuMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.flab.tableorder.mapper.OptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final CallRepository callRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;

    @Transactional(readOnly = true)
    public List<MenuCategoryDTO> getAllMenu(String storeId) {
        List<Category> categoryList = categoryRepository.findAllByStoreIdAndOptionFalse(new ObjectId(storeId));
        if (categoryList.isEmpty()) return new ArrayList<>();

        List<ObjectId> categoryIds = categoryList.stream()
            .map(category -> category.getCategoryId())
            .toList();

        List<Menu> menuList = menuRepository.findAllByCategoryIdIn(categoryIds);
        Map<String, List<Menu>> menuListMap = menuList.isEmpty()
            ? new HashMap<>()
            : menuList.stream()
                .collect(Collectors.groupingBy(menu -> menu.getCategoryId().toString()));

        return CategoryMapper.INSTANCE.toDTO(categoryList).stream()
            .map(menuCategoryDTO -> {
                List<MenuDTO> menus = Optional.ofNullable(menuListMap.get(menuCategoryDTO.getCategoryId()))
                    .map(menu -> MenuMapper.INSTANCE.toDTO(menu))
                    .orElseGet(() -> List.of());

                menuCategoryDTO.setMenu(menus);
                return menuCategoryDTO;
            })
            .toList();
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

        MenuDTO returnMenu = MenuMapper.INSTANCE.toDTO(menu);
        if (menu.isOptionEnabled()) {
            List<ObjectId> categoryIds = Optional.ofNullable(menu.getOptionCategoryIds())
                .filter(optionCategoryIds -> !optionCategoryIds.isEmpty())
                .orElse(List.of());

            Map<String, List<Option>> optionListMap = optionRepository.findAllByCategoryIdIn(categoryIds)
                .stream().collect(Collectors.groupingBy(option -> option.getCategoryId().toString()));

            returnMenu.setOptions(CategoryMapper.INSTANCE.toOptionDTO(categoryRepository.findAllByCategoryIdInAndOptionTrue(categoryIds))
                .stream()
                .map(categoryDTO -> {
                    List<OptionDTO> options = Optional.ofNullable(optionListMap.get(categoryDTO.getCategoryId()))
                        .map(option -> OptionMapper.INSTANCE.toDTO(option))
                        .orElseGet(() -> List.of());

                    categoryDTO.setOptions(options);
                    return categoryDTO;
                })
                .toList());
        }

        return returnMenu;
    }

    @Transactional(readOnly = true)
    public List<CallDTO> getAllCall(String storeId) {
        return CallMapper.INSTANCE.toDTO(callRepository.findAllByStoreId(new ObjectId(storeId)));
    }
}
