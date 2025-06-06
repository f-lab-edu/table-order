package com.flab.tableorder.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuCategoryDTO extends CategoryDTO implements Serializable {
    private List<MenuDTO> menu;
}
