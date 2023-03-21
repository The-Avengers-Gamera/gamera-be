package com.avengers.gamera.util;

import com.avengers.gamera.auth.GameraUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class CurrentUserController {
    public Long getUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = null;

        if (principal instanceof GameraUserDetails userDetails) {
            userId = userDetails.getId();
        }

        return userId;
    }

}