package com.flab.tableorder.domain;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends MongoRepository<Menu, ObjectId> {
    void deleteAllByCategoryIdIn(List<ObjectId> categoryIds);
    List<Menu> findAllByMenuIdIn(List<ObjectId> menuIds);
    List<Menu> findAllByCategoryIdIn(List<ObjectId> categoryIds);
    Optional<Menu> findByMenuId(ObjectId menuId);
}
