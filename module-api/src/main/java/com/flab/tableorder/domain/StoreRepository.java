package com.flab.tableorder.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
	Optional<Store> findByStoreId(Long storeId);
	Optional<Store> findByApiKey(String apiKey);
}
