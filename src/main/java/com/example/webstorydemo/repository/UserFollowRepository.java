package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.UserFollow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    Optional<UserFollow> findByStory_SlugAndUsers_Id(String slug, Long userId);

    @Query(value = "select f.story from UserFollow f " +
            "where f.users.id = :userId ")
    Page<Story> userGetByUserId(@Param("userId") Long userId,
                                Pageable pageable);
}
