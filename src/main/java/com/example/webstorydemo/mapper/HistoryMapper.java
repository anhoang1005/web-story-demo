package com.example.webstorydemo.mapper;

import com.example.webstorydemo.entity.ReadingHistory;
import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.StoryChapter;
import com.example.webstorydemo.model.history.HistoryDto;
import com.example.webstorydemo.utils.TimeMapperUtils;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {

    public HistoryDto entityToDto(ReadingHistory history){
        Story story = history.getStory();
        StoryChapter chapter = history.getChapter();
        return HistoryDto.builder()
                .historyId(history.getId())
                .storyTitle(story.getTitle())
                .storySlug(story.getSlug())
                .coverUrl(story.getCoverUrl())
                .chapter(chapter.getChapterNumber())
                .updatedAt(TimeMapperUtils.formatFacebookTime(history.getUpdatedAt()))
                .build();
    }
}
