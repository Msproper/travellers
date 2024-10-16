package com.example.demo;

public enum Colors {
    RED("red"),
    GREEN("green"),
    BLUE("blue"),
    WHITE("white"),
    BLACK("black");

    private final String description;

    Colors(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

