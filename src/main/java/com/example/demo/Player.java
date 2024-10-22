package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Player extends Cell{
    private boolean myTurn;
    private List<WayCell> possibleWays = new ArrayList<>();
    private byte winRow;
    private int numberOfWalls;

    public byte getWinRow() {
        return winRow;
    }

    public Player(int x, int y, Board board, Colors color, boolean myTurn, byte winRow) {
        super(x, y, board);
        this.winRow = winRow;
        applyStyle(color);
        this.myTurn = myTurn;

        this.numberOfWalls = Board.BOARD_SIZE/2;
    }

//    @Override
//    public void switchStatus() {
//
//    }


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
                if (!((WayCell)cell).isWall()) possibleWays.add((WayCell)cell);
            }
            else {
                for (Cell neighCell : cell.getNeighborhoodCells()){
                    if (!neighCell.getClass().isAssignableFrom(getClass())){
                        if (!((WayCell)neighCell).isWall()) possibleWays.add((WayCell)neighCell);

                    }
                }
            }
        }

    }

    public boolean checkLogic(){
        return Logic.bfs(board.getMatrix(), x, y, winRow);
    }


}
