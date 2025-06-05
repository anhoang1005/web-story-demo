package com.example.webstorydemo.services;

import com.example.webstorydemo.entity.ReadingHistory;
import com.example.webstorydemo.model.payload.ResponseBody;

public interface HistoryService {

    ResponseBody<?> userGetPageHistory(int pageNumber, int pageSize);
    void userUpdateHistory(Long userId, String storySlug, String chapterSlug);
    ResponseBody<?> userDeleteHistory(Long historyId);

    ReadingHistory findHistoryByStoryAndUser(String storySlug, Long userId);

}
