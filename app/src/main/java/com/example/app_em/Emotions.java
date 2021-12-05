package com.example.app_em;

public enum Emotions {
    BEST(5, "Лучше всех!"),
    PERFECT(4, "Отлично!"),
    GOOD(3, "Хорошо"),
    NORMAL(2, "Нормально"),
    SOSO(1, "Бывало и лучше"),
    BAD(0, "Плохо");


    Integer label;
    String decode;

    Emotions(Integer label, String decode) {
        this.label = label;
        this.decode = decode;
    }

}
