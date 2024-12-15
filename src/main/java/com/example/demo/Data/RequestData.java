package com.example.demo.Data;

import com.example.demo.Enums.DataType;

public class RequestData {

    private DataType dataType;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private String info;
    private GameDBModel gameDBModel;

    public String getInfo() {
        return info;
    }

    public GameDBModel getGameDBModel() {
        return gameDBModel;
    }

    public void setGameDBModel(GameDBModel gameDBModel) {
        this.gameDBModel = gameDBModel;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RequestData() {
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public RequestData(DataType dataType, int moveX, int moveY) {
        this.dataType = dataType;
        this.x = moveX;
        this.y = moveY;
    }

    public RequestData(DataType dataType, GameDBModel gameDBModel, String info){
        this.dataType = dataType;
        this.gameDBModel = gameDBModel;
        this.info = info;
    }

    public RequestData(DataType dataType, int x, int y, int x2, int y2) {
        this.dataType = dataType;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public RequestData(DataType dataType, String info) {
        this.dataType = dataType;
        this.info = info;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setX(int moveX) {
        this.x = moveX;
    }

    public void setY(int moveY) {
        this.y = moveY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}