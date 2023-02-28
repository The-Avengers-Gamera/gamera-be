package com.avengers.gamera.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "user_likes_article")
public class Like {

    @Id
    private Long userId;

    @Id
    private Long articleId;

    @Column(nullable = false, name = "created_time")
    @CreationTimestamp
    private OffsetDateTime createdTime;
}
