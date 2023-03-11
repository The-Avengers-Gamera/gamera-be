package com.avengers.gamera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingDto<E> {
    private E data;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
