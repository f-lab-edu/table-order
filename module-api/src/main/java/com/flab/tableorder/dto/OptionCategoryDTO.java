package com.flab.tableorder.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OptionCategoryDTO extends CategoryDTO {
	private List<OptionDTO> options;
}
