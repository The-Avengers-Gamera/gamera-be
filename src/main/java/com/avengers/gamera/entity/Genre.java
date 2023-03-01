package com.avengers.gamera.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "created_time")
    @CreationTimestamp
    private OffsetDateTime createdTime;

    @Column(nullable = false, name = "updated_time")
    @UpdateTimestamp
    private OffsetDateTime updatedTime;

    @ManyToMany(mappedBy = "genreList")
    @JsonBackReference
    private List<Game> gameList;

}
