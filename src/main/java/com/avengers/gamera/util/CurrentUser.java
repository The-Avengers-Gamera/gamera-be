package com.avengers.gamera.util;

import com.avengers.gamera.auth.GameraUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    public long getUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = 0;

        if (principal instanceof GameraUserDetails userDetails) {
            userId = userDetails.getId();
        }

        return userId;
    }

}