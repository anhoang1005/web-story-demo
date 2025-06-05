package com.example.webstorydemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "story_chapter", indexes = {
        @Index(name = "idx_story_chapter_slug", columnList = "slug")
})
public class StoryChapter extends BaseEntity<Long>{

    @Column(nullable = false)
    private Integer chapterNumber;

    @Column(nullable = false, length = 250)
    private String slug;

    @Column(nullable = false, length = 250)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @OneToOne(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ChapterContent chapterContent;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReadingHistory> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryComment> commentList = new ArrayList<>();

}
