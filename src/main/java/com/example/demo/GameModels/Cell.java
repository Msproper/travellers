package com.example.demo.GameModels;

import com.example.demo.Colors;
import javafx.scene.control.Label;


public abstract class Cell extends Label {

    protected int x;
    protected int y;
    protected Board board;
    private Cell[] neighborhoodCells = new Cell[4];
    private boolean hovered = false;

    protected Cell currentNeighbor;


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public int getIndex() {
        return x+y*Board.BOARD_SIZE;
    }

    public void setCoords(Cell cell) {
        x = cell.getX();
        y = cell.getY();
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void applyStyle(Colors color){
        setStyle("-fx-border-color: purple; -fx-background-color: "+color.getDescription());
    }

    public Cell(int x, int y, Board board){
        super();
        setMinSize(25, 25);
        setPrefSize(75, 75);
        setMaxSize(110, 110);
        this.x = x;
        this.y = y;
        this.board = board;



        setOnMouseEntered(_ -> {hovered = true; onMouseEntered();});
        setOnMouseExited(_ ->  {hovered = false; onMouseExited();});
        setOnMouseClicked(_ -> OnMouseClicked());
    }

    public abstract void onMouseEntered();

    public abstract void onMouseExited();

    public abstract void OnMouseClicked();

    public abstract void checkStyle(boolean mouseInOrOut);




    public Cell[] getNeighborhoodCells() {
        return neighborhoodCells;
    }

    public void setNeighborhoodCells(Cell[] neighborhoodCells) {
        this.neighborhoodCells = neighborhoodCells;
    }

    public void setCurrentNeighborhood(){
        currentNeighbor = neighborhoodCells[board.getBuildMode().ordinal()];
    }

}
