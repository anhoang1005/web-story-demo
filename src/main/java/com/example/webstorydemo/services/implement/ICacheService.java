package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.entity.Category;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.mapper.StoryMapper;
import com.example.webstorydemo.model.story.CategoryDto;
import com.example.webstorydemo.model.story.StoryRankDto;
import com.example.webstorydemo.model.story.TopStoryViewDTO;
import com.example.webstorydemo.repository.CategoryRepository;
import com.example.webstorydemo.repository.StoryViewRepository;
import com.example.webstorydemo.services.CacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ICacheService implements CacheService {
    private final StoryViewRepository storyViewRepository;
    private final CategoryRepository categoryRepository;
    private final StoryMapper storyMapper;

    @Override
    @Cacheable(value = "categoryCache")
    public List<CategoryDto> cacheCategory() {
        try{
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDto> dtoList = categories.stream()
                    .map(category -> new CategoryDto(category.getId(), category.getName(), category.getSlug()))
                    .collect(Collectors.toList());
            System.out.println("==> Cache category!");
            return dtoList;
        } catch (Exception e){
            log.error("Get all category error! " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "topStoriesToday", key = "#pageNumber + '-' + #pageSize")
    public List<StoryRankDto> cacheStoryRankInToday(int pageNumber, int pageSize) {
        try{
            Page<TopStoryViewDTO> page = storyViewRepository.getTopStoriesToday(
                    LocalDate.now(), PageRequest.of(pageNumber - 1, pageSize));
            System.out.println("==> Cache top day!");
            return page.stream()
                    .map(topStoryViewDTO -> storyMapper.entityToStoryRankDto(
                            topStoryViewDTO.getStory(), topStoryViewDTO.getTotalView()))
                    .collect(Collectors.toList());
        } catch (Exception e){
            log.error("Get top story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "topStoriesWeek", key = "#pageNumber + '-' + #pageSize")
    public List<StoryRankDto> cacheStoryRankInSevenDay(int pageNumber, int pageSize) {
        try{
            LocalDate end = LocalDate.now();
            LocalDate start = end.minusDays(6);
            Page<TopStoryViewDTO> page = storyViewRepository.getTopStoriesWithTotalViews(
                    start, end, PageRequest.of(pageNumber, pageSize));
            System.out.println("==> Cache top week!");
            return page.stream()
                    .map(topStoryViewDTO -> storyMapper.entityToStoryRankDto(
                            topStoryViewDTO.getStory(), topStoryViewDTO.getTotalView()))
                    .collect(Collectors.toList());
        } catch (Exception e){
            log.error("Get top story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "topStoriesMonth", key = "#pageNumber + '-' + #pageSize")
    public List<StoryRankDto> cacheStoryRankInThirtyDay(int pageNumber, int pageSize) {
        try{
            LocalDate end = LocalDate.now();
            LocalDate start = end.minusDays(29);
            Page<TopStoryViewDTO> page = storyViewRepository.getTopStoriesWithTotalViews(
                    start, end, PageRequest.of(pageNumber, pageSize));
            System.out.println("==> Cache top month!");
            return page.stream()
                    .map(topStoryViewDTO -> storyMapper.entityToStoryRankDto(
                            topStoryViewDTO.getStory(), topStoryViewDTO.getTotalView()))
                    .collect(Collectors.toList());
        } catch (Exception e){
            log.error("Get top story error! " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }
}
