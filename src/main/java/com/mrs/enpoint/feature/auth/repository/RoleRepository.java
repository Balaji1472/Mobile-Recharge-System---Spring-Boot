package com.mrs.enpoint.feature.auth.repository;

import com.mrs.enpoint.entity.Role;
import com.mrs.enpoint.feature.auth.enums.RoleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(RoleType roleName);

	@Query(value = """
			    SELECT r.role_name, COUNT(u.user_id)
			    FROM role r
			    LEFT JOIN users u ON u.role_id = r.role_id
			    GROUP BY r.role_name
			""", nativeQuery = true)
	List<Object[]> getRoleCountsRaw();
}
