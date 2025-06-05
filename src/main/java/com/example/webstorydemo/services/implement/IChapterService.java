package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.config.security.user_detail.CustomUserDetail;
import com.example.webstorydemo.entity.ChapterContent;
import com.example.webstorydemo.entity.Story;
import com.example.webstorydemo.entity.StoryChapter;
import com.example.webstorydemo.entity.StoryView;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.mapper.StoryMapper;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.model.story.ChapterDto;
import com.example.webstorydemo.model.story.ChapterShortDto;
import com.example.webstorydemo.model.story.CreateChapterRequest;
import com.example.webstorydemo.repository.StoryChapterRepository;
import com.example.webstorydemo.repository.StoryRepository;
import com.example.webstorydemo.repository.StoryViewRepository;
import com.example.webstorydemo.services.ChapterService;
import com.example.webstorydemo.services.HistoryService;
import com.example.webstorydemo.utils.AuthUtils;
import com.example.webstorydemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IChapterService implements ChapterService {
    private final StoryChapterRepository chapterRepository;
    private final StoryViewRepository storyViewRepository;
    private final StoryRepository storyRepository;

    private final AuthUtils authUtils;
    private final StoryMapper storyMapper;

    private final HistoryService historyService;

    @Async
    @Transactional
    public void addViewOfStory(Story story) {
        try{
            StoryView storyView = storyViewRepository.findStoryViewByViewDateAndStory(LocalDate.now(), story)
                    .orElse(null);
            if(storyView!=null){
                storyView.setViewCount(storyView.getViewCount() + 1);
            } else{
                storyView = new StoryView();
                storyView.setStory(story);
                storyView.setViewDate(LocalDate.now());
                storyView.setViewCount(1L);
            }
            story.setViewCount(story.getViewCount() + 1);
            storyViewRepository.save(storyView);
            storyRepository.save(story);
            System.out.println("Story: " + story.getTitle() + " + 1 view");
        } catch (Exception e){
            log.error("add view error! " + e.getMessage());
            throw new RequestNotFoundException("add view error");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> guestGetPageChapter(String storySlug, boolean isAsc,
                                               int pageNumber, int pageSize) {
        try{
            Sort sort;
            if(isAsc){
                sort = Sort.by(Sort.Order.asc("chapterNumber"));
            } else{
                sort = Sort.by(Sort.Order.desc("chapterNumber"));
            }
            Page<StoryChapter> page = chapterRepository.guestGetPageChapter(storySlug,
                    PageRequest.of(pageNumber - 1, pageSize, sort));
            List<ChapterShortDto> dtoList = page.stream()
                    .map(chapter -> new ChapterShortDto(chapter.getChapterNumber(), chapter.getSlug(),
                            chapter.getTitle(), TimeMapperUtils.localDateTimeToString(chapter.getCreatedAt())))
                    .collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(dtoList)
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get page chapter error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> guestGetChapterContent(String storySlug, String chapterSlug) {
        try{
            StoryChapter chapter = chapterRepository.guestGetChapterByStorySlugAndChapterSlug(storySlug, chapterSlug)
                    .orElseThrow(() -> new RequestNotFoundException("chapter not found"));
            ChapterDto chapterDto = storyMapper.entityToChapterDto(chapter);
            CustomUserDetail userDetail = authUtils.getUserFromAuthentication();
            if(userDetail!=null){
                Long userId = userDetail.getId();
                historyService.userUpdateHistory(userId, storySlug, chapterSlug);
            }
            addViewOfStory(chapter.getStory());
            return new ResponseBody<>(chapterDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get page chapter error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminUpdateChapter(Long chapterId, CreateChapterRequest request) {
        try{
            StoryChapter chapter = new StoryChapter();
            if(chapterId!=null){
                chapter = chapterRepository.findStoryChapterById(chapterId)
                        .orElseThrow(() -> new RequestNotFoundException("chapter not found"));
                if(request.getChapterNumber()!=null && !request.getChapterNumber().equals(chapter.getChapterNumber())){
                    chapter.setChapterNumber(request.getChapterNumber());
                    chapter.setSlug("chapter-" + request.getChapterNumber());
                }
                if(request.getContent()!=null && !request.getContent().equals(chapter.getChapterContent().getContent())){
                    chapter.setChapterContent(new ChapterContent(request.getContent(), chapter));
                }
            } else{
                chapter.setChapterNumber(request.getChapterNumber());
                chapter.setChapterContent(new ChapterContent(request.getContent(), chapter));
                chapter.setSlug("chapter-" + request.getChapterNumber());
            }
            chapter = chapterRepository.save(chapter);
            return new ResponseBody<>(storyMapper.entityToChapterDto(chapter));
        } catch (Exception e){
            log.error("update chapter error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> adminDeleteChapter(Long chapterId) {
        try {
            StoryChapter chapter = chapterRepository.findStoryChapterById(chapterId)
                    .orElseThrow(() -> new RequestNotFoundException("chapter not found"));
            chapterRepository.delete(chapter);
            return new ResponseBody<>("OK");
        } catch (Exception e){
            log.error("delete chapter error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }
}
