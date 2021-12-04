package com.example.app_em;

public enum Emotions {
    BEST(0, "Лучше всех!"),
    PERFECT(1, "Отлично!"),
    GOOD(2, "Хорошо!"),
    NORMAL(3, "Нормально!"),
    SOSO(5, "Бывало и лучше!"),
    BAD(4, "ПЛОХО!");


    Integer label;
    String decode;

    Emotions(Integer label, String decode) {
        this.label = label;
        this.decode = decode;
    }
        
}
