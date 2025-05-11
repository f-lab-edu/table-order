package com.flab.tableorder.service;

import com.flab.tableorder.DataLoader;
import com.flab.tableorder.domain.Category;
import com.flab.tableorder.domain.CategoryRepository;
import com.flab.tableorder.domain.Menu;
import com.flab.tableorder.domain.MenuRepository;
import com.flab.tableorder.domain.Store;
import com.flab.tableorder.domain.StoreRepository;
import com.flab.tableorder.dto.MenuCategoryDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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

        when(categoryRepository.findAllByStoreId(storeId)).thenReturn(new ArrayList<>());

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
            .map(Category::getCategoryId)
            .collect(Collectors.toList());

        when(categoryRepository.findAllByStoreId(storeId)).thenReturn(categoryList);
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
            .map(Category::getCategoryId)
            .collect(Collectors.toList());

        List<Menu> menuList = DataLoader.getMenuList(fileName);

        when(categoryRepository.findAllByStoreId(storeId)).thenReturn(categoryList);
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
    void getMenu_NotFound() throws IOException {
        // Assertions.assertThrows(EntityNotFoundException.class, () -> {
        // menuService.getMenu(0L);
        // });
    }

    @Test
    void getMenu_Success() throws IOException {
        // Store mockStore = DataLoader.getStoreInfo("store.json");
        // Long storeId = mockStore.getStoreId();
        // StoreContext.setStoreId(storeId);
        //
        // Menu mockMenu = mockStore.getCategories().get(0).getMenu().get(0);
        // Long menuId = mockMenu.getMenuId();
        //
        // when(menuRepository.findByMenuIdAndStore_StoreId(storeId, menuId)).thenReturn(Optional.of(mockMenu));
        //
        // MenuDTO menu = menuService.getMenu(menuId);
        //
        // assertThat(menu).isNotNull();
        // assertThat(menu).isEqualTo(MenuMapper.INSTANCE.toDTO(mockMenu));
    }

}
