package com.mrs.enpoint.feature.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mrs.enpoint.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	boolean existsByCategoryCode(String categoryCode);

	Optional<Category> findByCategoryCode(String categoryCode);

	// FETCH JOIN ensures category.getPlans() is populated in a single database hit
	@Query("""
			    SELECT DISTINCT c
			    FROM Category c
			    LEFT JOIN FETCH c.plans
			""")
	List<Category> findAllWithPlanCount();
}
