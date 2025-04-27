package com.flab.tableorder.controller;

import com.flab.tableorder.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        menu1.setTags(List.of("BEST"));

        MenuDTO menu2 = new MenuDTO();
        menu2.setMenuId(2);
        menu2.setMenuName("페퍼로니 피자");
        menu2.setPrice(9000);
        menu2.setImage("https://image.table-order.com/1_2.jpg");
        menu2.setSoldOut(false);
        menu2.setHasOption(true);
        menu2.setTags(List.of("NEW"));

        MenuCategoryDTO menuCategory1 = new MenuCategoryDTO();
        menuCategory1.setCategoryId(1);
        menuCategory1.setCategoryName("피자");
        menuCategory1.setMenu(List.of(menu1, menu2));

        MenuDTO menu3 = new MenuDTO();
        menu3.setMenuId(3);
        menu3.setMenuName("치즈 오븐 스파게티");
        menu3.setPrice(4000);
        menu3.setImage("https://image.table-order.com/1_3.jpg");
        menu3.setSoldOut(false);
        menu3.setHasOption(false);
        menu3.setTags(List.of("BEST", "SPICY"));

        MenuCategoryDTO menuCategory2 = new MenuCategoryDTO();
        menuCategory2.setCategoryId(2);
        menuCategory2.setCategoryName("사이드");
        menuCategory2.setMenu(List.of(menu3));



        ResponseDTO<List<MenuCategoryDTO>> responseData
                = new ResponseDTO<>(200, "", List.of(menuCategory1, menuCategory2));

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ResponseDTO<MenuDTO>> getMenuDetail(@PathVariable Long menuId) {
        OptionDTO option1 = new OptionDTO();
        option1.setOptionId(1);
        option1.setOptionName("치즈 추가");
        option1.setPrice(1000);
        option1.setImage("");
        option1.setSoldOut(false);
        option1.setOnlyOne(false);
        option1.setTags(List.of("BEST"));

        OptionDTO option2 = new OptionDTO();
        option2.setOptionId(2);
        option2.setOptionName("페퍼로니 추가");
        option2.setPrice(1000);
        option2.setImage("");
        option2.setSoldOut(false);
        option2.setOnlyOne(false);
        option2.setTags(List.of());

        OptionCategoryDTO optionCategory = new OptionCategoryDTO();
        optionCategory.setCategoryId(1);
        optionCategory.setCategoryName("토핑");
        optionCategory.setMultiple(true);
        optionCategory.setMaxSelect(0);
        optionCategory.setOptions(List.of(option1, option2));

        MenuDTO menu = new MenuDTO();
        menu.setMenuId(menuId);
        menu.setMenuName("페퍼로니 피자");
        menu.setDescription("짭짤한 페퍼로니가 들어간 피자");
        menu.setPrice(9000);
        menu.setSalePrice(8500);
        menu.setImage("https://image.table-order.com/1_2.jpg");
        menu.setTags(List.of("NEW"));
        menu.setOptions(List.of(optionCategory));


        ResponseDTO<MenuDTO> responseData
                = new ResponseDTO<>(200, "", menu);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/call")
    public ResponseEntity<ResponseDTO<List<CallDTO>>> getCallMenu() {
        CallDTO call1 = new CallDTO();
        call1.setCallId(1);
        call1.setCallName("물");
        
        CallDTO call2 = new CallDTO();
        call2.setCallId(2);
        call2.setCallName("젓가락");
        
        CallDTO call3 = new CallDTO();
        call3.setCallId(3);
        call3.setCallName("호출");

        ResponseDTO<List<CallDTO>> responseData
                = new ResponseDTO<>(200, "", List.of(call1, call2, call3));

        return ResponseEntity.ok(responseData);
    }
}
