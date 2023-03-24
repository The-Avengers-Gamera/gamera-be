package com.avengers.gamera.util;

import org.springframework.data.domain.Sort;

public class SortHelper {
    public static Sort.Direction getSortOrder(String order) {
        if ("desc".equals(order)) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
