package com.example.demo;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;


public class Cell extends Label {

    private static final byte OCCUPIED_VALUE = 1;
    private static final byte FREE_VALUE = 0;


    private int x;
    private int y;
    private boolean occupated;
    private Board board;
    private Cell[] neighborhoodCells = new Cell[4];
    private boolean greenPlayer = false;
    private boolean bluePlayer = false;
    private boolean hovered = false;

    public boolean isBluePlayer() {
        return bluePlayer;
    }

    public void setBluePlayer(boolean bluePlayer) {
        this.bluePlayer = bluePlayer;
        setCellFill(Colors.BLUE);
    }

    public boolean isGreenPlayer() {
        return greenPlayer;

    }

    public void setGreenPlayer(boolean greenPlayer) {
        this.greenPlayer = greenPlayer;
        setCellFill(Colors.GREEN);
    }

    public boolean isOccupated() {
        return occupated;
    }

    public void setOccupated(boolean occupated) {
        setCellFill(Colors.BLACK);
        this.occupated = occupated;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setCellFill(Colors color){
        if (isGreenPlayer()) color=Colors.GREEN;
        if (isBluePlayer()) color=Colors.BLUE;
        setStyle("-fx-border-color: black; -fx-background-color: "+color.getDescription()+';');

    }

    public boolean isPlayer(){
        return isBluePlayer() || isGreenPlayer();
    }

    public Cell(int x, int y, Board board){
        super();
        setCellFill(Colors.WHITE);
        setMinSize(25, 25);
        setMaxSize(70, 70);
        this.x = x;
        this.y = y;
        this.board = board;


        setOnMouseEntered(this::onMouseEntered);
        setOnMouseExited(this::onMouseExited);
        setOnMouseClicked(this::OnMouseClicked);
    }

    public void cleanBuildMode(){
        if(occupated) setCellFill(Colors.BLACK);
        else setCellFill(Colors.WHITE);

        if(getCurrentNeighborhood().isOccupated()) getCurrentNeighborhood().setCellFill(Colors.BLACK);
        else getCurrentNeighborhood().setCellFill(Colors.WHITE);
    }

    public void checkBuildMode(){
        if (occupated) setCellFill(Colors.RED);
        else setCellFill(Colors.BLACK);


        if (getCurrentNeighborhood().isOccupated()) getCurrentNeighborhood().setCellFill(Colors.RED);
        else getCurrentNeighborhood().setCellFill(Colors.BLACK);
    }

    public void setWall(){
        setCellFill(Colors.BLACK);
        occupated = true;
    }

    public void onMouseEntered(MouseEvent e){
        hovered = true;
        checkBuildMode();

    }

    private void onMouseExited(MouseEvent e){
        hovered = false;
        cleanBuildMode();

    }

    private void OnMouseClicked(MouseEvent e){
        Cell neighCell = getCurrentNeighborhood();
        if (!isOccupated() && !neighCell.isOccupated() && !neighCell.isPlayer() && !isPlayer()) {
            board.changeMatrix(neighCell.getX(), neighCell.getY(), OCCUPIED_VALUE);
            board.changeMatrix(x, y, OCCUPIED_VALUE);
            if (checkWay()) {
                setOccupated(true);
                getCurrentNeighborhood().setOccupated(true);
            } else {
                board.changeMatrix(x, y, FREE_VALUE);
                board.changeMatrix(neighCell.getX(), neighCell.getY(), FREE_VALUE);
                System.out.print("NOOO");
            }
        }

    }

    public Cell[] getNeighborhoodCells() {
        return neighborhoodCells;
    }

    public void setNeighborhoodCells(Cell[] neighborhoodCells) {
        this.neighborhoodCells = neighborhoodCells;
    }

    public Cell getCurrentNeighborhood(){
        return neighborhoodCells[board.getBuildMode().ordinal()];
    }

    private boolean checkWay(){
        return Logic.bfs(board.getMatrix(), board.getPosBlue(), (byte)0) && (Logic.bfs(board.getMatrix(), board.getPosGreen(), (byte)14));
    }



    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", occupated=" + occupated +
                ", board=" + board +
                "}\n";
    }
}
