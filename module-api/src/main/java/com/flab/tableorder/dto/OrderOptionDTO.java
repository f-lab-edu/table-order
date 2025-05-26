package com.flab.tableorder.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Data
public class OrderOptionDTO {
    private String optionId;
    private int quantity = 0;
    private int price;
}
