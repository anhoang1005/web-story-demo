package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryShortDto {
    private Long id;
    private String slug;
    private String title;
    @JsonProperty("cover_url")
    private String coverUrl;
    private Story.Status status;
    private Double rating;
    @JsonProperty("review_count")
    private Long reviewCount;
    @JsonProperty("view_count")
    private Long viewCount;
    @JsonProperty("follow_count")
    private Long followCount;
    @JsonProperty("chapter_update")
    private Integer chapterUpdate;
    @JsonProperty("chapter_updated_at")
    private String chapterUpdatedAt;
    @JsonProperty("created_at")
    private String createdAt;
}
