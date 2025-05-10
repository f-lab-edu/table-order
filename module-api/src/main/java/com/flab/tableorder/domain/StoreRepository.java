package com.flab.tableorder.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StoreRepository extends MongoRepository<Store, Long> {
	Optional<Store> findByApiKey(String apiKey);
}
