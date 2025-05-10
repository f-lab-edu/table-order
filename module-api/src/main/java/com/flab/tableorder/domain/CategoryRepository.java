package com.flab.tableorder.domain;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByStoreId(String storeId);
}