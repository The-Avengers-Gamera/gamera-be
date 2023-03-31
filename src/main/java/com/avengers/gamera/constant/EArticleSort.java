package com.avengers.gamera.constant;

public enum EArticleSort {
    CREATED_TIME("createdTime"),
    TITLE("title"),
    SCORES("game.scores");

    private final String name;

    EArticleSort(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EArticleSort getEnumByString(String type) {
        for (EArticleSort e : EArticleSort.values()) {
            if (e.name.equals(type)) return e;
        }
        return EArticleSort.CREATED_TIME;
    }
}
