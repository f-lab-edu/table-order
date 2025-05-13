package com.flab.tableorder.service;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.dto.MenuCategoryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.exception.MenuNotFoundException;
import com.flab.tableorder.exception.StoreNotFoundException;
import com.flab.tableorder.mapper.MenuMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void getAllMenu_Empty_Category() {
        Store mockStore = DataLoader.getStoreInfo("pizza.json");
        ObjectId objectId = mockStore.getStoreId();
        String storeId = objectId.toString();

        when(categoryRepository.findAllByStoreId(objectId)).thenReturn(new ArrayList<>());

        List<MenuCategoryDTO> allMenu = menuService.getAllMenu(storeId);
        assertThat(allMenu).isEmpty();
    }

    @Test
    void getAllMenu_Empty_Menu() {
        String fileName = "pizza.json";

        Store mockStore = DataLoader.getStoreInfo(fileName);
        ObjectId objectId = mockStore.getStoreId();
        String storeId = objectId.toString();

        List<Category> categoryList = DataLoader.getCategoryList(fileName);
        List<ObjectId> categoryIds = categoryList.stream()
            .map(category -> category.getCategoryId())
            .collect(Collectors.toList());

        when(categoryRepository.findAllByStoreId(objectId)).thenReturn(categoryList);
        when(menuRepository.findAllByCategoryIdIn(categoryIds)).thenReturn(new ArrayList<>());

        List<MenuCategoryDTO> allMenu = menuService.getAllMenu(storeId);

        assertThat(allMenu).isNotEmpty();
        assertThat(allMenu.size()).isEqualTo(categoryList.size());

        for (MenuCategoryDTO menuCategoryDTO : allMenu) {
            assertThat(menuCategoryDTO.getMenu()).isEmpty();
        }
    }

    @Test
    void getAllMenu_NotEmpty() {
        String fileName = "pizza.json";

        Store mockStore = DataLoader.getStoreInfo(fileName);
        ObjectId objectId = mockStore.getStoreId();
        String storeId = objectId.toString();

        List<Category> categoryList = DataLoader.getCategoryList(fileName);
        List<ObjectId> categoryIds = categoryList.stream()
            .map(category -> category.getCategoryId())
            .collect(Collectors.toList());

        List<Menu> menuList = DataLoader.getMenuList(fileName);

        when(categoryRepository.findAllByStoreId(objectId)).thenReturn(categoryList);
        when(menuRepository.findAllByCategoryIdIn(categoryIds)).thenReturn(menuList);

        List<MenuCategoryDTO> allMenu = menuService.getAllMenu(storeId);

        assertThat(allMenu).isNotEmpty();
        assertThat(allMenu.size()).isEqualTo(categoryList.size());

        int menuCnt = 0;
        for (MenuCategoryDTO menuCategoryDTO : allMenu) {
            assertThat(menuCategoryDTO.getMenu()).isNotEmpty();
            menuCnt += menuCategoryDTO.getMenu().size();
        }
        assertThat(menuList.size()).isEqualTo(menuCnt);
    }

    @Test
    void getMenu_NotFound_Menu() {
        ObjectId menuId = new ObjectId("111111111111111111111111");
        when(menuRepository.findByMenuId(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.getMenu("invalidStoreId", menuId.toString()))
            .isInstanceOf(MenuNotFoundException.class)
            .hasMessageStartingWith("Menu not found for menuId:");
    }

    @Test
    void getMenu_NotFound_Category() {
        String fileName = "pizza.json";
        Menu mockMenu = DataLoader.getMenuList(fileName).get(0);

        ObjectId menuId = mockMenu.getMenuId();
        when(menuRepository.findByMenuId(menuId)).thenReturn(Optional.of(mockMenu));
        when(categoryRepository.findByCategoryId(mockMenu.getCategoryId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuService.getMenu("invalidStoreId", menuId.toString()))
            .isInstanceOf(StoreNotFoundException.class)
            .hasMessageStartingWith("Store not found for categoryId:")
        ;
    }

    @Test
    void getMenu_Mismatch() {
        String fileName = "pizza.json";
        Menu mockMenu = DataLoader.getMenuList(fileName).get(0);
        Category mockCategory = new Category();
        mockCategory.setCategoryId(mockMenu.getCategoryId());
        mockCategory.setStoreId(new ObjectId("111111111111111111111111"));

        ObjectId menuId = mockMenu.getMenuId();
        when(menuRepository.findByMenuId(menuId)).thenReturn(Optional.of(mockMenu));
        when(categoryRepository.findByCategoryId(mockMenu.getCategoryId())).thenReturn(Optional.of(mockCategory));

        assertThatThrownBy(() -> menuService.getMenu("222222222222222222222222", menuId.toString()))
            .isInstanceOf(StoreNotFoundException.class)
            .hasMessageStartingWith("Store mismatch:")
        ;
    }

    @Test
    void getMenu_Success() {
        String fileName = "pizza.json";
        Menu mockMenu = DataLoader.getMenuList(fileName).get(0);

        ObjectId categoryId = mockMenu.getCategoryId();
        ObjectId menuId = mockMenu.getMenuId();

        String storeId = "";
        Category mockCategory = null;
        for (Category category : DataLoader.getCategoryList(fileName)) {
            if (category.getCategoryId().equals(categoryId)) {
                mockCategory = category;
                storeId = category.getStoreId().toString();
                break;
            }
        }

        when(menuRepository.findByMenuId(menuId)).thenReturn(Optional.of(mockMenu));
        when(categoryRepository.findByCategoryId(categoryId)).thenReturn(Optional.of(mockCategory));

        MenuDTO menu = menuService.getMenu(storeId, menuId.toString());

        assertThat(menu).isNotNull();
        assertThat(menu).isEqualTo(MenuMapper.INSTANCE.toDTO(mockMenu));
    }

}
