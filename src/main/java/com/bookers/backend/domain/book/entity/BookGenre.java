package com.bookers.backend.domain.book.entity;

public enum BookGenre {
    PHILOSOPHY("철학"),
    ECONOMY("경제/경영"),
    NOVEL("소설"),
    HISTORY("역사"),
    SCIENCE("과학"),
    ART("예술"),
    SELF_HELP("자기계발"),
    ETC("기타");

    private final String description;

    BookGenre(String description){
        this.description=description;
    }

    public String getDescription(){
        return description;
    }
}
