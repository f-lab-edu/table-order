package com.flab.tableorder.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Data
public class StoreDTO {
	private String storeId = "";
	private String apiKey = "";

	private List<MenuCategoryDTO> categories;
}
