package com.avengers.gamera.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PagingDto<T> {
    private T data;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
