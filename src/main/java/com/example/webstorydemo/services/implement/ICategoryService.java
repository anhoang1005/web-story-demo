package com.example.webstorydemo.services.implement;

import com.example.webstorydemo.entity.Category;
import com.example.webstorydemo.exceptions.request.RequestNotFoundException;
import com.example.webstorydemo.model.story.CategoryDto;
import com.example.webstorydemo.repository.CategoryRepository;
import com.example.webstorydemo.services.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ICategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public List<CategoryDto> guestGetAllCategory() {
        try{
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDto> dtoList = categories.stream()
                    .map(category -> new CategoryDto(category.getId(), category.getName(), category.getSlug()))
                    .collect(Collectors.toList());
            return dtoList;
        } catch (Exception e){
            log.error("Get all category error! " + e.getMessage());
            throw new RequestNotFoundException(e.getMessage());
        }
    }
}
