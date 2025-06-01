package com.flab.tableorder.document;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends MongoRepository<Store, ObjectId> {
    void deleteAllByStoreId(ObjectId storeId);
    Optional<Store> findByApiKey(String apiKey);
}
