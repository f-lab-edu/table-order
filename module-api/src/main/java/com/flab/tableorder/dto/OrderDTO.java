package com.flab.tableorder.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class OrderDTO {
    private String menuId;
    private int quantity;
    private int price;
    private List<OrderOptionDTO> options;
}