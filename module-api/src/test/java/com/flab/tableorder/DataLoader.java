package com.flab.tableorder;

import com.flab.tableorder.domain.*;
import com.flab.tableorder.dto.*;
import com.flab.tableorder.mapper.*;

import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataLoader {

	public static Store getStoreInfo(ObjectMapper objectMapper) {
		InputStream inputStream = DataLoader.class.getResourceAsStream("/store_1.json");

		Store store = null;
		try {
			StoreDTO storeDTO = objectMapper.readValue(inputStream, new TypeReference<>() {});
			store = StoreMapper.INSTANCE.toEntity(storeDTO);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return store;
	}
}
