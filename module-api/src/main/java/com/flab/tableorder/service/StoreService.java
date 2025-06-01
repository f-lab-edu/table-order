package com.flab.tableorder.service;

import com.flab.tableorder.repository.CallRepository;
import com.flab.tableorder.repository.CategoryRepository;
import com.flab.tableorder.repository.MenuRepository;
import com.flab.tableorder.repository.OptionRepository;
import com.flab.tableorder.document.Store;
import com.flab.tableorder.repository.StoreRepository;
import com.flab.tableorder.exception.StoreNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final MenuRepository menuRepository;
    private final OptionRepository optionRepository;
    private final CallRepository callRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "apiKey", key = "#apiKey")
    public String getStoreIdByApiKey(String apiKey) {
        log.debug("캐시에 API Key가 존재하지 않음... DB Select");

        Store store = storeRepository.findByApiKey(apiKey)
            .orElseThrow(() -> new StoreNotFoundException("요청하신 API 키로 등록된 매장을 찾을 수 없습니다."));

        return store.getStoreId().toString();
    }

    @Transactional
    public boolean deleteAllStore(String storeId) {
        ObjectId objStoreId = new ObjectId(storeId);

        callRepository.deleteAllByStoreId(objStoreId);
        optionRepository.deleteAllByCategoryIdIn(categoryRepository.findAllByStoreIdAndOptionTrue(objStoreId)
            .stream()
            .map(category -> category.getCategoryId())
            .toList());
        menuRepository.deleteAllByCategoryIdIn(categoryRepository.findAllByStoreIdAndOptionFalse(objStoreId)
            .stream()
            .map(category -> category.getCategoryId())
            .toList());
        categoryRepository.deleteAllByStoreId(objStoreId);
        storeRepository.deleteAllByStoreId(objStoreId);

        return true;
    }
}
