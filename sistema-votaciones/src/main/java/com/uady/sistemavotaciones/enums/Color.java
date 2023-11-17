package com.uady.sistemavotaciones.enums;

public enum Color {

    RED("#FF0000"),
    GREEN("#00FF00"),
    BLUE("#0000FF"),
    YELLOW("#FFFF00"),
    ORANGE("#FFA500"),
    PURPLE("#800080");

    public final String hexCode;

    Color(String hexCode) {
        this.hexCode = hexCode;
    }

}
