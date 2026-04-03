package com.mrs.enpoint.feature.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrs.enpoint.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByCategoryCode(String categoryCode);

    Optional<Category> findByCategoryCode(String categoryCode);
}
