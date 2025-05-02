package com.flab.tableorder.mapper;

import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;

import java.util.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    List<MenuCategoryDTO> toDTO(List<MenuCategory> menuCategories);

    MenuDTO toDTO(Menu menu);
}