package com.avengers.gamera.exception;

import org.springframework.security.access.AccessDeniedException;

public class GameraAccessDeniedException extends AccessDeniedException {

    public GameraAccessDeniedException() {
        super("No permission");
    }
}
