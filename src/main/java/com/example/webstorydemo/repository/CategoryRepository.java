package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM category ORDER BY RAND() LIMIT :randomCount", nativeQuery = true)
    List<Category> findRandomCategories(@Param("randomCount") Integer randomCount);

    Optional<Category> findCategoryBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids")
    List<Category> findByIdIn(@Param("ids") List<Long> ids);

}
