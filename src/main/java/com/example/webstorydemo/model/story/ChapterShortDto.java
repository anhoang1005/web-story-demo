package com.example.webstorydemo.model.story;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ChapterShortDto {

    public ChapterShortDto(Integer chapterNumber, String slug, String title, String createdAt) {
        this.chapterNumber = chapterNumber;
        this.slug = slug;
        this.title = title;
        this.createdAt = createdAt;
    }

    @JsonProperty("chapter_number")
    private Integer chapterNumber;
    private String slug;
    private String title;
    @JsonProperty("created_at")
    private String createdAt;
}
