package com.flab.tableorder.mapper;

import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;

import org.bson.types.ObjectId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ObjectIdMapper.class)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "categoryId", target = "categoryId")
    List<MenuCategoryDTO> toDTO(List<Category> menuCategories);
}