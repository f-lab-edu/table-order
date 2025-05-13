package com.flab.tableorder.domain;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, ObjectId> {
    List<Category> findAllByStoreId(ObjectId storeId);
    Optional<Category> findByCategoryId(ObjectId categoryId);
}
