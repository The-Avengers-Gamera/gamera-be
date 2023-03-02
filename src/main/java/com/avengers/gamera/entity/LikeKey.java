package com.avengers.gamera.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikeKey implements Serializable {

    private Long userId;

    private Long articleId;

}
