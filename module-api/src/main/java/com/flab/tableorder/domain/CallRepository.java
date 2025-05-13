package com.flab.tableorder.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends MongoRepository<Call, ObjectId> {
}
