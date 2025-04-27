package com.flab.tableorder.controller;

import com.flab.tableorder.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
