package com.avengers.gamera.dto.user;

import com.avengers.gamera.dto.article.MiniArticleGetDto;
import com.avengers.gamera.dto.authority.AuthoritySlimDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private long id;
    private Set<AuthoritySlimDto> authorities;
    private String name;
    private String email;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer postsCount;
    private List<MiniArticleGetDto> likesArticlesDto;
    private List<MiniArticleGetDto> commentsArticlesDto;
    private List<MiniArticleGetDto> postsArticlesDto;
    private OffsetDateTime updatedTime;

    public void setFromUserGetDto(UserGetDto userGetDto) {
        this.id = userGetDto.getId();
        this.authorities = userGetDto.getAuthorities();
        this.name = userGetDto.getName();
        this.email = userGetDto.getEmail();
        this.updatedTime = userGetDto.getUpdatedTime();
    }
}
