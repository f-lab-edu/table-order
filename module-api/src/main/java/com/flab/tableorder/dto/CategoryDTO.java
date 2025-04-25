package com.flab.tableorder.dto;

import lombok.*;

@Getter
@Setter
public class CategoryDTO {
	private long categoryId;
	private String categoryName;
	private boolean isMultiple;
	private int maxSelect;
}
