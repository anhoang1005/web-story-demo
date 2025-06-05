package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.StoryView;
import com.example.webstorydemo.model.story.TopStoryViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface StoryViewRepository extends JpaRepository<StoryView, Long> {

    Optional<StoryView> findStoryViewByViewDateAndStory(LocalDate date, Story story);

    @Query("""
        SELECT sv.story AS story, sv.viewCount AS totalView
        FROM StoryView sv WHERE sv.viewDate = :today
        ORDER BY sv.viewCount DESC
    """)
    Page<TopStoryViewDTO> getTopStoriesToday(@Param("today") LocalDate today, Pageable pageable);


    @Query("""
        SELECT sv.story AS story, SUM(sv.viewCount) AS totalView
        FROM StoryView sv
        WHERE sv.viewDate BETWEEN :startDate AND :endDate
        GROUP BY sv.story
        ORDER BY SUM(sv.viewCount) DESC
    """)
    Page<TopStoryViewDTO> getTopStoriesWithTotalViews(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate,
                                                      Pageable pageable);

    //Top thang
    @Query("""
        SELECT sv.story AS story, SUM(sv.viewCount) AS totalView
        FROM StoryView sv
        WHERE FUNCTION('MONTH', sv.viewDate) = :month
          AND FUNCTION('YEAR', sv.viewDate) = :year
        GROUP BY sv.story
        ORDER BY SUM(sv.viewCount) DESC
    """)
    Page<TopStoryViewDTO> getTopStoriesThisMonth(@Param("month") int month,
                                       @Param("year") int year,
                                       Pageable pageable);



}
