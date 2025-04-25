package com.flab.tableorder.dto;

import lombok.*;

@Getter
@Setter
public class MenuCategoryDTO extends CategoryDTO {
	private MenuDTO[] menu;
}
