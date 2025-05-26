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
    public ResponseEntity<ResponseDTO<List<MenuCategoryDTO>>> getAllMenu() {
        List<MenuCategoryDTO> menuList = menuService.getAllMenu(StoreContext.getStoreId());

        ResponseDTO<List<MenuCategoryDTO>> responseData = new ResponseDTO<>(200, "", menuList);

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
        List<CallDTO> callList = menuService.getAllCall(StoreContext.getStoreId());

        ResponseDTO<List<CallDTO>> responseData = new ResponseDTO<>(200, "", callList);

        return ResponseEntity.ok(responseData);
    }
}
