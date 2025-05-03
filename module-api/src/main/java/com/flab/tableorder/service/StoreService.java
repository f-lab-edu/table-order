package com.flab.tableorder.service;

import com.flab.tableorder.domain.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j @RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "storeCache", key = "#apiKey")
	public Long getStoreIdByApiKey(String apiKey) {
		log.info("캐시에 API Key가 존재하지 않음... DB Select");

		Store store = storeRepository.findByApiKey(apiKey)
				.orElseThrow(() -> new EntityNotFoundException("Api Key not found"));

		return store.getStoreId();
	}
}
