package com.flab.tableorder.controller;

import com.flab.tableorder.context.StoreContext;
import com.flab.tableorder.dto.OrderDTO;
import com.flab.tableorder.dto.ResponseDTO;

import java.util.List;

import com.flab.tableorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/table/{tableNum}")
    public ResponseEntity<ResponseDTO<List<OrderDTO>>> postOrder(@RequestBody List<OrderDTO> requestData, @PathVariable int tableNum) {
        List<OrderDTO> orderListAll = orderService.orderMenu(requestData, StoreContext.getStoreId(), tableNum);

        //        TODO: POS기로 주문 정보 전송
        ResponseDTO<List<OrderDTO>> responseData = new ResponseDTO<>(200, "", orderListAll);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/table/{tableNum}")
    public ResponseEntity<ResponseDTO> getOrderList(@PathVariable int tableNum) {
        orderService.getOrderList(StoreContext.getStoreId(), tableNum);

        ResponseDTO responseData = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/table/{tableNum}/call")
    public ResponseEntity<ResponseDTO> postCall(@RequestBody List<String> requestData, @PathVariable String tableNum) {
        orderService.orderCall(requestData, StoreContext.getStoreId(), Integer.parseInt(tableNum));
        ResponseDTO responseData = new ResponseDTO<>(200, "");

        return ResponseEntity.ok(responseData);
    }
}
