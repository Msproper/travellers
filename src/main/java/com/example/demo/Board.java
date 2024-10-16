package com.example.demo;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {

    private byte[][] matrix = new byte[15][15];



    private WallBuildMode buildMode = WallBuildMode.LEFT;
    private final int BOARD_SIZE = 15;
    private Cell[] cells = new Cell[225];
    private int currentTurn = 1;
    private final int FIRST_PLAYER_POS = 7;
    private final int SECOND_PLAYER_POS = 218;
    private byte[] posGreen = {7, 0};
    private byte[] posBlue = {7, 14};

    public Board(){
        setStyle("-fx-border-color: black; -fx-background-color: white;");
        for (int i= 0; i<BOARD_SIZE*BOARD_SIZE; i++){
            int x = getX(i);
            int y = getY(i);
            Cell cell = new Cell(x, y, this);
            add(cell, x, y);
            cells[i] = cell;
            if (i == FIRST_PLAYER_POS) {
                cell.setGreenPlayer(true);
            }
            if (i == SECOND_PLAYER_POS) {
                cell.setBluePlayer(true);
            }


        }
        createMatrix();
        setNeighborhoodsForCells();
    }

    public byte[] getPosBlue() {
        return posBlue;
    }

    public void setPosBlue(byte[] posBlue) {
        this.posBlue = posBlue;
    }

    public byte[] getPosGreen() {
        return posGreen;
    }

    public void setPosGreen(byte[] posGreen) {
        this.posGreen = posGreen;
    }

    public byte[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(byte[][] matrix) {
        this.matrix = matrix;
    }

    public void createMatrix(){
        for (Cell cell: cells){
            int x = cell.getX();
            int y = cell.getY();
            if (cell.isOccupated()) matrix[y][x] = 1;
            if (cell.isBluePlayer()) matrix[y][x] = 2;
            if (cell.isGreenPlayer()) matrix[y][x]= 3;
        }
    }

    public void changeMatrix(int x, int y, byte value){
        matrix[y][x] = value;
    }

    public WallBuildMode getBuildMode() {
        return buildMode;
    }
    public void switchBuildMode() {
        for (Cell cell: cells){
            cell.cleanBuildMode();
        }

        buildMode = switch (buildMode){
            case LEFT -> WallBuildMode.UP;
            case DOWN -> WallBuildMode.LEFT;
            case UP -> WallBuildMode.RIGHT;
            case RIGHT -> WallBuildMode.DOWN;
        };

        for (Cell cell: cells){
            if (cell.isHovered()) cell.checkBuildMode();
        }


    }

    private void setNeighborhoodsForCells(){
        for (int i= 0; i<BOARD_SIZE*BOARD_SIZE; i++){
            Cell[] neighborhoodCells = new Cell[4];
            if (i - 15 >= 0) neighborhoodCells[0] = cells[i-15];
            else neighborhoodCells[0] = cells[i+15];

            if (i+15 <= 224) neighborhoodCells[1] = cells[i+15];
            else neighborhoodCells[1] = cells[i-15];

            if ((i+1) % 15 == 0  && i != 0 ) neighborhoodCells[2] = cells[i-1];
            else neighborhoodCells[2] = cells[i+1];

            if (i % 15 == 0 && i != 224 || i == 0) neighborhoodCells[3] = cells[i+1];
            else neighborhoodCells[3] = cells[i-1];

            cells[i].setNeighborhoodCells(neighborhoodCells);
        }

    }

    public void printMatrix(){
        for (byte[] row: matrix){
            for (byte value: row){
                System.out.print(value);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }

    private int getX(int i){
        return i % BOARD_SIZE;
    }

    private int getY(int i){
        return (i-getX(i))/BOARD_SIZE;
    }

}
