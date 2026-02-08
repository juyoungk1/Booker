package com.bookers.backend.domain.book.entity;

public enum Visibility {
    PUBLIC("전체공개"),
    FRIENDS("친구에게만 공개"),
    PRIVATE("비공개");

    private final String description;

    Visibility(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
}
