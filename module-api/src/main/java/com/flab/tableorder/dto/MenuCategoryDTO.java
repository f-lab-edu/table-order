package com.flab.tableorder.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class MenuCategoryDTO extends CategoryDTO {
	private List<MenuDTO> menu;
}
