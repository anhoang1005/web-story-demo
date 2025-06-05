package com.example.webstorydemo.model.story;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CategoryDto {

    public CategoryDto(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    private Long id;
    private String name;
    private String slug;
}
