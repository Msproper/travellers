package com.example.demo.Enums;

public enum StatusText {
    TURN_GREEN("Ход зеленых"),
    TURN_BLUE("Ход синих"),
    NOWAY("Невозможный ход:\nнет возможных \nходов"),
    DONT_HAVE_WALLS("Невозможный ход:\n закончились стены"),
    WALL_COLLISION("Невозможный ход:\nмешает другая \nстена");


    private final String description;

    StatusText(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
