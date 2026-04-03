package com.mrs.enpoint.feature.operator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrs.enpoint.entity.Operator;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Integer> {
    boolean existsByOperatorName(String operatorName);
}
