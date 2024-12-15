package com.example.demo.GameModels;

import com.example.demo.Data.GameDBModel;
import com.example.demo.Data.PlayerDTO;
import com.example.demo.Enums.Colors;
import com.example.demo.Utilits.Logic;

import java.util.ArrayList;
import java.util.List;

public class Player extends Cell {
    private boolean myTurn;
    private final List<WayCell> possibleWays = new ArrayList<>();
    private byte winRow;
    private int numberOfWalls;
    private int timer;
    private final Colors color;


    public Player(int x, int y, Board board, Colors color, boolean myTurn, byte winRow, GameModel gameModel) {
        super(x, y, board, gameModel);
        this.winRow = winRow;
        applyStyle(color);
        this.color = color;
        this.myTurn = myTurn;
        timer = 6000*3;

        this.numberOfWalls = 8;
    }

    public Player(GameModel gameModel, PlayerDTO playerDTO, Colors color, Board board){
        super(playerDTO.getX(), playerDTO.getY(), board, gameModel);
        applyStyle(color);
        this.myTurn = playerDTO.isMyTurn();
        this.winRow = playerDTO.getWinRow();
        this.numberOfWalls = playerDTO.getNumberOfWalls();
        this.timer = playerDTO.getTimer();
        this.color = color;
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
        if (color == Colors.BLUE) gameModel.getWallsOfBlue().setText(String.valueOf(numberOfWalls));
        else gameModel.getWallsOfGreen().setText(String.valueOf(numberOfWalls));
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

    public PlayerDTO getPlayerDTOFromPlayer(){
        return new PlayerDTO(x, y, myTurn, winRow, numberOfWalls, timer);
    }




}
