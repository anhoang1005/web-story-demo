package com.example.webstorydemo.repository;

import com.example.webstorydemo.entity.StoryComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<StoryComment, Long> {

    @Query(value = "select c from StoryComment c " +
            "where c.story.slug = :storySlug " +
            "and (:chapterSlug is null or c.chapter.slug = :chapterSlug) " +
            "and (:parentId is null or c.parentComment.id = :parentId) ")
    Page<StoryComment> guestGetPageComment(@Param("storySlug") String storySlug,
                                           @Param("chapterSlug") String chapterSlug,
                                           @Param("parentId") Long parentId,
                                           Pageable pageable);

    @Query(value = "select c from StoryComment c " +
            "where c.id = :commentId " +
            "and c.users.id = :userId")
    Optional<StoryComment> checkExistedComment(@Param("userIDd") Long userId,
                                               @Param("commentId") Long commentId);
}
