package com.flab.tableorder.dto;

import lombok.*;

@Getter
@Setter
public class CategoryDTO {
	private long categoryId = 0;
	private String categoryName = "";
	// isMultiple == true : 다중 선택
	private boolean isMultiple = false;
	// isMultiple == true : 필수 선택
	private boolean isRequired = false;
	// maxSelect == 0 : 수량 제한 없음
	private int maxSelect = 0;
}