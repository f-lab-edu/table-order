package com.flab.tableorder.mapper;

import com.flab.tableorder.dto.*;
import com.flab.tableorder.domain.*;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    Store toEntity(StoreDTO storeDTO);
}