package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.entity.*;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.mapper.HistoryMapper;
import com.example.webstorydemo.model.history.HistoryDto;
import com.example.webstorydemo.model.payload.PageData;
import com.example.webstorydemo.model.payload.ResponseBody;
import com.example.webstorydemo.repository.HistoryRepository;
import com.example.webstorydemo.repository.StoryChapterRepository;
import com.example.webstorydemo.repository.UserRepository;
import com.example.webstorydemo.services.HistoryService;
import com.example.webstorydemo.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IHistoryService implements HistoryService {
    private final UserRepository userRepository;
    private final StoryChapterRepository chapterRepository;
    private final HistoryRepository historyRepository;

    private final AuthUtils authUtils;
    private final HistoryMapper historyMapper;

    @Override
    @Transactional
    public ResponseBody<?> userGetPageHistory(int pageNumber, int pageSize) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,
                    Sort.by(Sort.Order.desc("updatedAt")));
            Page<ReadingHistory> page = historyRepository.userGetPageHistory(userId, pageable);
            List<HistoryDto> dtoList = page.stream()
                    .map(historyMapper::entityToDto).collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(dtoList)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("get history error! " + e.getMessage());
            throw new RequestNotFoundException("get history error! " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void userUpdateHistory(Long userId, String storySlug, String chapterSlug) {
        try {
            Users users = userRepository.findUsersById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("user not found"));
            ReadingHistory history = historyRepository.userGetHistoryBySlug(userId, storySlug)
                    .orElse(null);
            StoryChapter chapter = chapterRepository.guestGetChapterByStorySlugAndChapterSlug(storySlug, chapterSlug)
                    .orElseThrow(() -> new RequestNotFoundException("chapter not found!"));
            if(history!=null){
                StoryChapter old = history.getChapter();
                if(old.getChapterNumber() < chapter.getChapterNumber())
                history.setChapter(chapter);
            } else{
                history = new ReadingHistory();
                Story story = chapter.getStory();
                history.setStory(story);
                history.setChapter(chapter);
                history.setUsers(users);
            }
            historyRepository.save(history);
        } catch (Exception e){
            log.error("get history error! " + e.getMessage());
            throw new RequestNotFoundException("get history error! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userDeleteHistory(Long historyId) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            ReadingHistory history = historyRepository.userGetHistory(userId, historyId)
                    .orElseThrow(() -> new RequestNotFoundException("history not found"));
            historyRepository.delete(history);
            return new ResponseBody<>("OK");
        } catch (Exception e){
            log.error("get history error! " + e.getMessage());
            throw new RequestNotFoundException("get history error! " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ReadingHistory findHistoryByStoryAndUser(String storySlug, Long userId) {
        try {
            return historyRepository.userGetHistoryBySlug(userId, storySlug)
                    .orElse(null);
        } catch (Exception e){
            log.error("get history error! " + e.getMessage());
            throw new RequestNotFoundException("get history error! " + e.getMessage());
        }
    }
}
