package com.avengers.gamera.constant;

public enum EUserArticleType {
    LIKES("likes"),
    COMMENTS("comments"),
    POSTS("posts");

    private final String name;

    EUserArticleType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static String getEnumByString(String type) {
        for (EUserArticleType e : EUserArticleType.values()) {
            if (e.name.equals(type)) return e.name();
        }
        return null;
    }
}
