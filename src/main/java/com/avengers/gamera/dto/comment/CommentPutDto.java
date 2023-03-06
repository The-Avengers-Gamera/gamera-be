package com.avengers.gamera.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentPutDto {
    @NotBlank(message = "Comment text must be provided.")
    @Size(max = 800, message = "Comment text must be less than 800 characters.")
    private String text;
}
