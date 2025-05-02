package com.flab.tableorder.domain;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	Optional<Menu> findByStore_storeIdAndMenuId(Long storeId, Long menuId);

}