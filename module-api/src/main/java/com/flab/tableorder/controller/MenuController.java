package com.flab.tableorder.controller;

import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.dto.CallDTO;
import com.flab.tableorder.dto.MenuCategoryDTO;
import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.ResponseDTO;
import com.flab.tableorder.service.MenuService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity getAllMenu() {
        List<MenuCategoryDTO> menuList = menuService.getAllMenu(StoreContext.getStoreId());

        ResponseDTO responseData = new ResponseDTO<>(200, "", menuList);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<ResponseDTO<MenuDTO>> getMenuByMenuId(@PathVariable String menuId) {
        MenuDTO menu = menuService.getMenu(StoreContext.getStoreId(), menuId);

        ResponseDTO<MenuDTO> responseData = new ResponseDTO<>(200, "", menu);

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
