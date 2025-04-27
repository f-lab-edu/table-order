package com.flab.tableorder.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
public class MenuDTO {
	private long menuId = 0;
	private String menuName = "";
	private String description = "";
	private int quantity = 0;
	private int price = 0;
	private int salePrice = 0;
	private String image = "";
	private boolean isSoldOut = false;
	private boolean hasOption = false;
	private List<String> tags;
	private List<OptionCategoryDTO> options;
}
