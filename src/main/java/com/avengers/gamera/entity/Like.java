package com.avengers.gamera.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Entity
@IdClass(LikeKey.class)
@Table(name = "user_likes_article")
public class Like implements Serializable {

    @Id
    private Long userId;

    @Id
    private Long articleId;

    @Column(nullable = false, name = "created_time")
    @CreationTimestamp
    private OffsetDateTime createdTime;
}
