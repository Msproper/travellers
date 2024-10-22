package com.example.demo;

public enum StatusText {
    TURN_GREEN("Ход зеленых"),
    TURN_BLUE("Ход синих"),
    NOWAY("Невозможный ход:\n нет возможных ходов"),
    WALL_COLLISION("Невозможный ход:\n мешает другая стена");


    private final String description;

    StatusText(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
