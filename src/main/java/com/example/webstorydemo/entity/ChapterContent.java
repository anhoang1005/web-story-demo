package com.example.webstorydemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chapter_content")
public class ChapterContent extends BaseEntity<Long>{

    public ChapterContent(String content, StoryChapter chapter) {
        this.content = content;
        this.chapter = chapter;
    }

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private StoryChapter chapter;
}
