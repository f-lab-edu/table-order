package com.flab.tableorder.mapper;

import com.flab.tableorder.dto.*;
import com.flab.tableorder.domain.*;

import org.bson.types.ObjectId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ObjectIdMapper.class)
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    @Mapping(source = "storeId", target = "storeId")
    Store toEntity(StoreDTO storeDTO);
    @Mapping(source = "storeId", target = "storeId")
    StoreDTO toDTO(Store store);

//    @AfterMapping
//    default void linkEntities(@MappingTarget Store store) {
//        for (Category category : store.getCategories()) {
//            if (category.getStore() != null) continue;
//
//            category.setStore(store);
//            for (Menu menu : category.getMenu()) {
//                if (menu.getCategory() == null) menu.setCategory(category);
//                if (menu.getStore() == null) menu.setStore(store);
//            }
//        }
//    }
}