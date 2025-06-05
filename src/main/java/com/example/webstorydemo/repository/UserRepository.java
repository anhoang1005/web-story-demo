package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Roles;
import com.example.webstorydemo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersById(Long userId);

    Optional<Users> findUsersByUsername(String username);

    Optional<Users> findUsersByEmail(String email);

    Optional<Users> findUsersByUsernameOrEmail(String username, String email);

    @Query(value = "select u from Users u " +
            "where (:username is null or u.username like %:username%) " +
            "and (:email is null or u.email like %:email%) " +
            "and (:status is null or u.status = :status) " +
            "and (:role is null or :role member of u.rolesList)")
    Page<Users> adminGetPageUser(@Param("username") String username,
                                 @Param("email") String email,
                                 @Param("role") Roles role,
                                 @Param("status") Users.Status status,
                                 Pageable pageable);
}
