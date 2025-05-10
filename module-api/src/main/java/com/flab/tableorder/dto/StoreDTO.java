package com.flab.tableorder.dto;

import java.util.*;

import lombok.*;

@Getter @Setter @Data
public class StoreDTO {
	private String storeId = "";
	private String apiKey = "";

	private List<MenuCategoryDTO> categories;
}
