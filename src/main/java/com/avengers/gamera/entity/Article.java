package com.avengers.gamera.entity;

import com.avengers.gamera.constant.EArticleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private int likeNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EArticleType type;

    @OneToMany(mappedBy = "article")
    private List<Comment> commentList;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> tagList;

    @Column(name = "comment_num")
    private int commentNum;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    @Column(nullable = false, name = "created_time")
    @CreationTimestamp
    private OffsetDateTime createdTime;

    @Column(nullable = false, name = "updated_time")
    @UpdateTimestamp
    private OffsetDateTime updatedTime;

    @ManyToMany(mappedBy = "likedArticles")
    @Builder.Default
    @JsonBackReference
    List<User> likeUsers= new ArrayList<>();
}
