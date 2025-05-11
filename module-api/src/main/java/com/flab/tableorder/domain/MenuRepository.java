package com.flab.tableorder.domain;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends MongoRepository<Menu, Long> {
    List<Menu> findAllByCategoryIdIn(List<ObjectId> categoryIds);
    Optional<Menu> findByMenuIdAndStore_StoreId(String storeId, String menuId);
}
