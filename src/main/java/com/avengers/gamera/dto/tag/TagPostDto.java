package com.avengers.gamera.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagPostDto {
    @NotBlank
    @Size(max = 30, message = "Tag name can not be more than 30 characters.")
    private String name;

}
