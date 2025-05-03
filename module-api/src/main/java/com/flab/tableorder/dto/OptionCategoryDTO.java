package com.flab.tableorder.dto;

import java.util.List;

import lombok.*;

@Getter @Setter
public class OptionCategoryDTO extends CategoryDTO {
	private List<OptionDTO> options;
}
