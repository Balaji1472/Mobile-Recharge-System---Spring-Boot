package com.mrs.enpoint.feature.auth.repository;

import com.mrs.enpoint.entity.Role;
import com.mrs.enpoint.feature.auth.enums.RoleType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(RoleType roleName);
}
