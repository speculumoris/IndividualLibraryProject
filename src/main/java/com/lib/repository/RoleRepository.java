package com.lib.repository;

import com.lib.domain.Role;
import com.lib.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {


    Optional<Role> findByRoleType(RoleType roleType);
}

