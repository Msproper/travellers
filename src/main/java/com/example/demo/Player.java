package com.example.demo;

import com.example.demo.GameModels.Board;
import com.example.demo.GameModels.Cell;

import java.util.ArrayList;
import java.util.List;

public class Player extends Cell {
    private boolean myTurn;
    private final List<WayCell> possibleWays = new ArrayList<>();
    private byte winRow;
    private int numberOfWalls;
    private int timer;
    private final Colors color;


    public Player(int x, int y, Board board, Colors color, boolean myTurn, byte winRow) {
        super(x, y, board);
        this.winRow = winRow;
        applyStyle(color);
        this.color = color;
        this.myTurn = myTurn;
        timer = 6000*3;

        this.numberOfWalls = Board.BOARD_SIZE/2;
    }

    public byte getWinRow() {
        return winRow;
    }

    public void updateTimer() {
        this.timer--;
    }

    public boolean isWin(){
        return getWinRow() == getY();
    }

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public void useWall() {
        numberOfWalls--;
        System.out.print(numberOfWalls);
    }

    public boolean isLose(){
        return timer < 0;
    }

    public int getTimer() {
        return timer;
    }

    public List<WayCell> getPossibleWays() {
        return possibleWays;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    @Override
    public void onMouseEntered() {
        if (!myTurn) return;
        for(WayCell way : possibleWays){
            way.applyStyle(Colors.GRAY);
        }
    }

    @Override
    public void onMouseExited() {
        if (!myTurn) return;
        if (!board.isChoiced()) {
            for (WayCell way : possibleWays){
                way.applyStyle(Colors.WHITE);
            }
        }
    }

    @Override
    public void OnMouseClicked() {
        if (!myTurn) return;
        board.setChoiced(!board.isChoiced());
        findPossibleWays();
    }

    @Override
    public void checkStyle(boolean mouseInOrOut
    ) {

    }

    @Override
    public String toString() {
        return "Player X="+getX()+"  Y="+getY();
    }

    public void findPossibleWays(){
        possibleWays.clear();
        for (Cell cell: getNeighborhoodCells()){
            if (!cell.getClass().isAssignableFrom(getClass())){
                if (((WayCell) cell).isWall()) possibleWays.add((WayCell)cell);
            }
            else {
                for (Cell neighCell : cell.getNeighborhoodCells()){
                    if (!neighCell.getClass().isAssignableFrom(getClass())){
                        if (((WayCell) neighCell).isWall()) possibleWays.add((WayCell)neighCell);

                    }
                }
            }
        }

    }

    public boolean checkLogic(){
        return Logic.bfs(board.getMatrix(), x, y, winRow);
    }


}
