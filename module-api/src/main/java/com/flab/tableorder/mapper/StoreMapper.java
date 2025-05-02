package com.flab.tableorder.mapper;

import com.flab.tableorder.dto.*;
import com.flab.tableorder.domain.*;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreDTO storeDTO);
}