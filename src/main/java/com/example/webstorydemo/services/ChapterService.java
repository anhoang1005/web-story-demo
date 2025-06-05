package com.example.webstorydemo.services;

import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.story.CreateChapterRequest;

public interface ChapterService {

    ResponseBody<?> guestGetPageChapter(String storySlug, boolean isAsc, int pageNumber, int pageSize);
    ResponseBody<?> guestGetChapterContent(String storySlug, String chapterSlug);

    ResponseBody<?> adminUpdateChapter(Long chapterId, CreateChapterRequest request);
    ResponseBody<?> adminDeleteChapter(Long chapterId);
}
