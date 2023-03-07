package com.avengers.gamera.entity;

import com.avengers.gamera.constant.ArticleType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
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
    private User user;

    @Column(name = "cover_img_url")
    private String coverImgUrl;

    @Column
    private String title;

    @Column
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleType type;

    @OneToMany(mappedBy = "article")
    private List<Comment> commentList;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    @Column(nullable = false, name = "created_time")
    @CreationTimestamp
    private OffsetDateTime createdTime;

    @Column(nullable = false, name = "updated_time")
    @UpdateTimestamp
    private OffsetDateTime updatedTime;
}
