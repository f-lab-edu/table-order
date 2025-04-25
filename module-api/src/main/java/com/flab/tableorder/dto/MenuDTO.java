package com.flab.tableorder.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
public class MenuDTO {
	private long menuId;
	private String menuName;
	private String description;
	private int quantity;
	private int price;
	private int salePrice;
	private String image;
	private boolean isSoldOut;
	private boolean hasOption;
	private List<String> tags;
	private OptionDTO[] options;
}
