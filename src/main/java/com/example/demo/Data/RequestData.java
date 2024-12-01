package com.example.demo.Data;

public class RequestData {
    private int moveX;
    private int moveY;

    public RequestData() {
    }

    public RequestData(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public void setMoveX(int moveX) {
        this.moveX = moveX;
    }

    public void setMoveY(int moveY) {
        this.moveY = moveY;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
}