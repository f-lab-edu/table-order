package com.flab.tableorder.domain;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	List<Menu> findAllByCategoryIdIn(List<ObjectId> categoryIds);
	Optional<Menu> findByMenuIdAndStore_StoreId(String storeId, String menuId);
}