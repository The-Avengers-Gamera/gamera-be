package com.avengers.gamera.entity;

import com.avengers.gamera.constant.ArticleType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
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
