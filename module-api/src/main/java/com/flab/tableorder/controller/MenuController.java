package com.flab.tableorder.controller;

import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/menu")
public class MenuController {

    @GetMapping
    public ResponseEntity<ResponseDTO<List<MenuCategoryDTO>>> getMenu() {
        MenuDTO menu1 = new MenuDTO();
        menu1.setMenuId(1);
        menu1.setMenuName("치즈 피자");
        menu1.setPrice(8000);
        menu1.setImage("https://image.table-order.com/1_1.jpg");
        menu1.setSoldOut(false);
        menu1.setHasOption(true);
        menu1.setTags(List.of(new String[]{"BEST"}));

        MenuDTO menu2 = new MenuDTO();
        menu2.setMenuId(2);
        menu2.setMenuName("페퍼로니 피자");
        menu2.setPrice(9000);
        menu2.setImage("https://image.table-order.com/1_2.jpg");
        menu2.setSoldOut(false);
        menu2.setHasOption(true);
        menu2.setTags(List.of(new String[]{"NEW"}));

        MenuCategoryDTO menuCategory1 = new MenuCategoryDTO();
        menuCategory1.setCategoryId(1);
        menuCategory1.setCategoryName("피자");
        menuCategory1.setMenu(new MenuDTO[]{menu1, menu2});

        MenuDTO menu3 = new MenuDTO();
        menu3.setMenuId(3);
        menu3.setMenuName("치즈 오븐 스파게티");
        menu3.setPrice(4000);
        menu3.setImage("https://image.table-order.com/1_3.jpg");
        menu3.setSoldOut(false);
        menu3.setHasOption(false);
        menu3.setTags(List.of(new String[]{"BEST", "SPICY"}));

        MenuCategoryDTO menuCategory2 = new MenuCategoryDTO();
        menuCategory2.setCategoryId(2);
        menuCategory2.setCategoryName("사이드");
        menuCategory2.setMenu(new MenuDTO[]{menu3});



        ResponseDTO<List<MenuCategoryDTO>> response
                = new ResponseDTO<List<MenuCategoryDTO>>(200, "", List.of(new MenuCategoryDTO[]{menuCategory1, menuCategory2}));

        return ResponseEntity.ok(response);
    }
}
