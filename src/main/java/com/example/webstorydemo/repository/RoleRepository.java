package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findRolesByRoleName(Roles.BaseRole roleName);
    Boolean existsRolesByRoleName(Roles.BaseRole role);

}
