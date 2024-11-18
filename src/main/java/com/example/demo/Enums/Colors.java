package com.example.demo.Enums;

public enum Colors {
    RED("red;"),
    GREEN("green;"),
    BLUE("blue;"),
    WHITE("white;"),
    BLACK("black;"),
    GRAY("#808080;"),
    GOLD("DAA520;");

    private final String description;

    Colors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

