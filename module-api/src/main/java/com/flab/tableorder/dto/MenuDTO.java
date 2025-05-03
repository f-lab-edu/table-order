package com.flab.tableorder.dto;

import java.util.List;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter @Setter @Data
public class MenuDTO {
	private long menuId = 0;
	private String menuName = "";
	private String description = "";
	private int quantity = 0;
	private int price = 0;
	private int salePrice = 0;
	private String image = "";
	@JsonProperty("isSoldOut")
	private boolean soldOut = false;
	@JsonProperty("isOptionEnabled")
	private boolean optionEnabled = false;
	private List<String> tags;
	private List<OptionCategoryDTO> options;
}
