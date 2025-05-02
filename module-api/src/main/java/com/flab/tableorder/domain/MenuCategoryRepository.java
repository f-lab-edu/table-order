package com.flab.tableorder.domain;

import com.flab.tableorder.dto.*;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
	List<MenuCategoryDTO> findAllByCategoryId(Long categoryId);
}
