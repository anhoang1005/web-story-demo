package com.example.webstorydemo.model.story;

import com.example.webstorydemo.entity.Story;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryRankDto {
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
    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("rank_view")
    private Long rankView;

    public enum TopRank{
        VIEW_ALL_DAY,
        VIEW_ONE_DAY,
        VIEW_SEVEN_DAY,
        VIEW_THIRTY_DAY,
        VIEW_CURRENT_MONTH,

        STATUS_COMPLETED,
        CREATED_AT_ALL,
        UPDATED_At_ALL,
        FOLLOW_ALL,
        COMMENT_ALL,

        CHAPTER_COUNT,

    }
}
