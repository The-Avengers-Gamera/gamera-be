package com.avengers.gamera.util;

import org.springframework.security.core.context.SecurityContextHolder;
import com.avengers.gamera.auth.GameraUserDetails;

public class CurrentUser {
    public static Long getUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;

        if (principal instanceof GameraUserDetails userDetails) {
            userId = userDetails.getId();
        }

        return userId;
    }
}
