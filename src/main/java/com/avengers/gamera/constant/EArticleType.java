package com.avengers.gamera.constant;

public enum EArticleType {
    NEWS("news"),
    REVIEW("reviews");

    private final String name;

    EArticleType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static String getEnumByString(String type) {
        for (EArticleType e : EArticleType.values()) {
            if (e.name.equals(type)) return e.name();
        }
        return null;
    }
}
