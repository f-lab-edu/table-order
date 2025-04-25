package com.flab.tableorder.dto;

import lombok.*;

@Getter
@Setter
public class OptionCategoryDTO extends CategoryDTO {
	private OptionDTO[] options;
}
