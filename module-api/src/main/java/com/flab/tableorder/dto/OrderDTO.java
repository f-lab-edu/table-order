package com.flab.tableorder.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class OrderDTO {
    private String menuId;
    private int quantity;
    private int price;
    private List<OrderOptionDTO> options;
}