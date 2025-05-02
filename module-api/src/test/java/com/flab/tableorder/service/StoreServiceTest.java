package com.flab.tableorder.service;

import com.flab.tableorder.domain.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
	@Mock
	private StoreRepository storeRepository;
	@InjectMocks
	private StoreService storeService;

	@Test
	void getApiKey_NotFound() {
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			storeService.getStoreIdByApiKey("notfound");
		});
	}

	@Test
	void getApiKey_Success() {
		String apiKey = "testapikey1";
		Long mockStoreId = 1L;

		Store store = new Store();
		store.setStoreId(mockStoreId);
		store.setApiKey(apiKey);

		when(storeRepository.findByApiKey(apiKey)).thenReturn(Optional.of(store));

		Long storeId = storeService.getStoreIdByApiKey(apiKey);

		assertThat(storeId).isEqualTo(mockStoreId);
	}
}
