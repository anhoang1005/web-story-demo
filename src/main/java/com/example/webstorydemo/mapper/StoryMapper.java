package com.example.webstorydemo.mapper;

import com.example.webstorydemo.entity.Category;
import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.StoryChapter;
import com.example.webstorydemo.model.story.*;
import com.example.webstorydemo.utils.TimeMapperUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoryMapper {

    public StoryShortDto entityToStoryShortDto(Story story){
        return StoryShortDto.builder()
                .id(story.getId())
                .slug(story.getSlug())
                .title(story.getTitle())
                .coverUrl(story.getCoverUrl())
                .status(story.getStatus())
                .rating(story.getRating())
                .followCount(story.getFollowCount())
                .viewCount(story.getViewCount())
                .reviewCount(story.getReviewCount())
                .chapterUpdate(story.getChapterUpdate())
                .chapterUpdatedAt(TimeMapperUtils.formatFacebookTime(story.getChapterUpdateAt()))
                .createdAt(TimeMapperUtils.formatFacebookTime(story.getCreatedAt()))
                .build();
    }

    public StoryRankDto entityToStoryRankDto(Story story, Long rankView){
        return StoryRankDto.builder()
                .id(story.getId())
                .slug(story.getSlug())
                .title(story.getTitle())
                .coverUrl(story.getCoverUrl())
                .status(story.getStatus())
                .rating(story.getRating())
                .followCount(story.getFollowCount())
                .viewCount(story.getViewCount())
                .reviewCount(story.getReviewCount())
                .chapterUpdate(story.getChapterUpdate())
                .rankView(rankView)
                .createdAt(TimeMapperUtils.formatFacebookTime(story.getCreatedAt()))
                .build();
    }

    public StoryDto entityToStoryDto(Story story){
        List<Category> categoryList = story.getCategoryList();
//        double rating = 0.0;
//        if(story.getReviewCount()!= 0){
//            rating = (double) story.getStarCount() / story.getReviewCount();
//        }
        return StoryDto.builder()
                .id(story.getId())
                .slug(story.getSlug())
                .title(story.getTitle())
                .author(story.getAuthor())
                .coverUrl(story.getCoverUrl())
                .description(story.getDescription())
                .status(story.getStatus())
                .rating(story.getRating())
                .followCount(story.getFollowCount())
                .viewCount(story.getViewCount())
                .reviewCount(story.getReviewCount())
                .chapterUpdate(story.getChapterUpdate())
                .categoryList(categoryList.stream()
                        .map(category -> new CategoryDto(category.getId(), category.getName(), category.getSlug()))
                        .collect(Collectors.toList()))
                .chapterList(null)
                .createdAt(TimeMapperUtils.formatFacebookTime(story.getCreatedAt()))
                .chapterUpdatedAt(TimeMapperUtils.formatFacebookTime(story.getChapterUpdateAt()))
                .build();
    }

    public ChapterDto entityToChapterDto(StoryChapter chapter){
        Story story = chapter.getStory();
        return ChapterDto.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .chapterNumber(chapter.getChapterNumber())
                .slug(chapter.getSlug())
                .maxChapter(story.getChapterUpdate())
                .storyTitle(story.getTitle())
                .content(chapter.getChapterContent().getContent())
                .createdAt(TimeMapperUtils.localDateTimeToString(chapter.getCreatedAt()))
                .build();
    }
}
