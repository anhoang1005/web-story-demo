package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.ReadingHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<ReadingHistory, Long> {

    @Query(value = "select h from ReadingHistory h " +
            "where h.users.id = :userId")
    Page<ReadingHistory> userGetPageHistory(@Param("userId") Long userId,
                                            Pageable pageable);

    @Query(value = "select h from ReadingHistory h " +
            "where h.users.id = :userId " +
            "and h.story.slug = :storySlug")
    Optional<ReadingHistory> userGetHistoryBySlug(@Param("userId") Long userId,
                                                  @Param("storySlug") String storySlug);

    @Query(value = "select h from ReadingHistory h " +
            "where h.users.id = :userId " +
            "and h.id = :historyId")
    Optional<ReadingHistory> userGetHistory(@Param("userId") Long userId,
                                            @Param("historyId") Long historyId);
}
