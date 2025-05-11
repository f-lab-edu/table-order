package com.flab.tableorder.controller;

import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.ResponseDTO;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {
    @PostMapping("/table/{tableId}")
    public ResponseEntity<ResponseDTO<Map<String, List<MenuDTO>>>> postOrder(@RequestBody Map<String, List<MenuDTO>> requestData) {
        ResponseDTO<Map<String, List<MenuDTO>>> responseData
                = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/table/{tableId}/call")
    public ResponseEntity<ResponseDTO<Map<String, List<Long>>>> postCall(@RequestBody Map<String, List<Long>> requestData) {
        ResponseDTO<Map<String, List<Long>>> responseData
                = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }
}
