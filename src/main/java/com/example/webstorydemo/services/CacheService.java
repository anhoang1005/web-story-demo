package com.example.webstorydemo.services;

import com.example.webstorydemo.model.story.CategoryDto;
import com.example.webstorydemo.model.story.StoryRankDto;

import java.util.List;

public interface CacheService {

    List<CategoryDto> cacheCategory();
    List<StoryRankDto> cacheStoryRankInToday(int pageNumber, int pageSize);
    List<StoryRankDto> cacheStoryRankInSevenDay(int pageNumber, int pageSize);
    List<StoryRankDto> cacheStoryRankInThirtyDay(int pageNumber, int pageSize);
}
