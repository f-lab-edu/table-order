package com.flab.tableorder.mapper;

import com.flab.tableorder.dto.*;
import com.flab.tableorder.domain.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreDTO storeDTO);
    StoreDTO toDTO(Store store);

    @AfterMapping
    default void linkEntities(@MappingTarget Store store) {
        for (MenuCategory category : store.getCategories()) {
            if (category.getStore() != null) continue;

            category.setStore(store);
            for (Menu menu : category.getMenu()) {
                if (menu.getCategory() == null) menu.setCategory(category);
                if (menu.getStore() == null) menu.setStore(store);
            }
        }
    }
}