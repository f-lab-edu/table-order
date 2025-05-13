package com.flab.tableorder.domain;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store, ObjectId> {
    Optional<Store> findByApiKey(String apiKey);
}
