package com.flab.tableorder.dto;

import java.util.List;

import lombok.*;

@Getter @Setter
public class MenuCategoryDTO extends CategoryDTO {
	private List<MenuDTO> menu;
}
