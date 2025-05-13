package com.flab.tableorder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class MenuDTO {
    private String menuId;
    private String menuName;
    private String description;
    private int quantity = 0;
    private int price;
    private int salePrice = 0;
    private String image;
    private boolean soldOut = false;
    private boolean optionEnabled = false;
    private List<String> tags;
    private List<OptionCategoryDTO> options;
}
