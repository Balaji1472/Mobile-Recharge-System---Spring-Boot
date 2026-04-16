package com.mrs.enpoint.feature.operator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrs.enpoint.entity.Operator;
import com.mrs.enpoint.feature.auth.enums.Status;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    boolean existsByOperatorName(String operatorName);
    
    List<Operator> findByStatus(Status status);
    
    @Query("SELECT DISTINCT o FROM Operator o LEFT JOIN FETCH o.plans")
    List<Operator> findAllWithPlans();
}
