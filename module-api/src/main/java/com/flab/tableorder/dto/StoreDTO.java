package com.flab.tableorder.dto;

import java.util.*;

import lombok.*;

@Getter @Setter
public class StoreDTO {
	private long storeId = 0;
	private String apiKey = "";

	private List<MenuCategoryDTO> categories;
}
