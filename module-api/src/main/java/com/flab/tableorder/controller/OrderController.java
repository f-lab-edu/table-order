package com.flab.tableorder.controller;

import com.flab.tableorder.dto.MenuDTO;
import com.flab.tableorder.dto.ResponseDTO;

import java.util.List;
import java.util.Map;

import com.flab.tableorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/table/{tableId}")
    public ResponseEntity<ResponseDTO> postOrder(@RequestBody Map<String, List<MenuDTO>> requestData) {
        ResponseDTO responseData = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/table/{tableId}/call")
    public ResponseEntity<ResponseDTO> postCall(@RequestBody Map<String, List<Long>> requestData) {
        ResponseDTO responseData = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }
}
