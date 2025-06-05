package com.example.webstorydemo.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageData<T> {
    @JsonProperty("data")
    private T data;
    @JsonProperty("total_data")
    private Long totalData;
    @JsonProperty("total_page")
    private Integer totalPage;
    @JsonProperty("page_number")
    private Integer pageNumber;
    @JsonProperty("page_size")
    private Integer pageSize;
}
