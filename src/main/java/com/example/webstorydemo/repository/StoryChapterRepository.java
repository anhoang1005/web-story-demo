package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.StoryChapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryChapterRepository extends JpaRepository<StoryChapter, Long> {

    Optional<StoryChapter> findStoryChapterById(Long id);

    @Query(value = "select c from StoryChapter c " +
            "where c.story.slug = :storySlug")
    Page<StoryChapter> guestGetPageChapter(@Param("storySlug") String storySlug,
                                           Pageable pageable);

    @Query(value = "select c from StoryChapter c " +
            "where c.story.slug = :storySlug " +
            "and  c.slug = :chapterSlug")
    Optional<StoryChapter> guestGetChapterByStorySlugAndChapterSlug(@Param("storySlug") String storySlug,
                                                                   @Param("chapterSlug") String chapterSlug);

    @Query(value = "SELECT * FROM story_chapter ORDER BY RAND() LIMIT 20", nativeQuery = true)
    List<StoryChapter> findRandom20Chapters();

}
