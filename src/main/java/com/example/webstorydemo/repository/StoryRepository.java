package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Category;
import com.example.webstorydemo.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story,Long> {

    Optional<Story> findStoryBySlug(String slug);

    @Query(value = "select s from Story s " +
            "where (:title is null or s.title like %:title%) " +
            "and (:status is null or s.status = :status) " +
            "and (:category is null or :category member of s.categoryList)")
    Page<Story> guestGetFilterStory(@Param("title") String title,
                                    @Param("status") Story.Status status,
                                    @Param("category") Category category,
                                    Pageable pageable);

    @Query(value = "SELECT * FROM story ORDER BY RAND() LIMIT :randomCount", nativeQuery = true)
    List<Story> findRandomStory(@Param("randomCount") Integer randomCount);


}
