package com.flab.tableorder.mapper;

import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;

import java.util.*;

import org.bson.types.ObjectId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ObjectIdMapper.class)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    @Mapping(source = "menuId", target = "menuId")
    List<MenuDTO> toDTO(List<Menu> menuList);

    @Mapping(source = "menuId", target = "menuId")
    MenuDTO toDTO(Menu menu);
}