package com.example.webstorydemo.services;

import com.example.webstorydemo.model.story.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> guestGetAllCategory();
}
