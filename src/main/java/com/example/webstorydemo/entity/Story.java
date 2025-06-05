package com.example.webstorydemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "story", indexes = {
        @Index(name = "idx_story_slug", columnList = "slug", unique = true),
        @Index(name = "idx_story_author", columnList = "author"),
        @Index(name = "idx_story_status", columnList = "status")
    }
)
public class Story extends BaseEntity<Long>{

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)", unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Long starCount;

    @Column(nullable = false)
    private Long reviewCount;

    @Column(nullable = false)
    private Long followCount;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private Integer chapterUpdate;

    private LocalDateTime chapterUpdateAt;

    @Getter
    public enum Status{
        DANG_TIEN_HANH,
        HOAN_THANH,
        DUNG_CAP_NHAT
    }

    @Column(nullable = false)
    public Boolean enable;

    //Story - Category
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "story_category",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryChapter> chapterList = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserFollow> favoritesList = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryView> storyViewList = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReadingHistory> historyList = new ArrayList<>();

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryComment> commentList = new ArrayList<>();

    @PrePersist
    public void preCreate(){
        this.starCount = 0L;
        this.reviewCount = 0L;
        this.rating = 0.0;
        this.enable = true;
    }
}
