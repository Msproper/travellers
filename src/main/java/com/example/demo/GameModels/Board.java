package com.example.demo.GameModels;

import com.example.demo.*;
import com.example.demo.Enums.StatusText;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;


public class Board extends GridPane {

    private WallBuildMode buildMode = WallBuildMode.LEFT;
    public final static int BOARD_SIZE = MainClass.BOARD_SIZE;
    private byte[][] matrix = new byte[BOARD_SIZE][BOARD_SIZE];
    private final Cell[] cells = new Cell[BOARD_SIZE*BOARD_SIZE];
    private final Player bluePlayer;
    private final Player greenPlayer;
    private boolean choiced = false;
    private final MainClass mainClass;
    Timeline timer;

    public Board(MainClass mainClass){

        this.mainClass = mainClass;

        setMinSize(BOARD_SIZE*20, BOARD_SIZE*20);
        setMaxSize(BOARD_SIZE*50, BOARD_SIZE*50);
        bluePlayer = new Player(BOARD_SIZE/2, BOARD_SIZE-1, this, Colors.BLUE, true, (byte)0);
        greenPlayer = new Player(BOARD_SIZE/2, 0, this, Colors.GREEN, false, (byte)(BOARD_SIZE-1));

        add(greenPlayer, BOARD_SIZE/2, 0);
        add(bluePlayer, BOARD_SIZE/2, BOARD_SIZE-1);

        int FIRST_PLAYER_POS = BOARD_SIZE / 2;
        cells[FIRST_PLAYER_POS] = greenPlayer;
        int SECOND_PLAYER_POS = BOARD_SIZE * (BOARD_SIZE - 1) + FIRST_PLAYER_POS;
        cells[SECOND_PLAYER_POS] = bluePlayer;

        for (int i= 0; i<BOARD_SIZE*BOARD_SIZE; i++){

            int x = getX(i);
            int y = getY(i);
            if (i != FIRST_PLAYER_POS && i != SECOND_PLAYER_POS) {
                Cell cell = new WayCell(x, y, this);
                add(cell, x, y);
                cells[i] = cell;
            }



        }
        setStatus(bluePlayer.isMyTurn() ? StatusText.TURN_BLUE : StatusText.TURN_GREEN,
               bluePlayer.isMyTurn() ? Colors.BLUE : Colors.GREEN );
        setNeighborhoodsForCells();
        startTimer();

    }

    public void setStatus(StatusText statusText, Colors color){
        mainClass.getStatus().setText(statusText.getDescription());
        mainClass.getStatus().setStyle("-fx-text-fill: "+color.getDescription() + "-fx-background-color: #AACC99;");


    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getGreenPlayer() {
        return greenPlayer;
    }

    public boolean isChoiced() {
        return choiced;
    }

    public void setChoiced(boolean choiced) {
        this.choiced = choiced;
    }

    public byte[][] getMatrix() {
        return matrix;
    }


    public void changeMatrix(int x, int y, byte value){
        matrix[y][x] = value;
    }

    public void checkPossibleWaysForBoth(){
        greenPlayer.findPossibleWays();
        bluePlayer.findPossibleWays();
    }

    public void checkWin(){
        if (bluePlayer.isWin() || greenPlayer.isLose()){
            DoWin(Colors.BLUE);
            return;
        }
        if (greenPlayer.isWin() || bluePlayer.isLose()){
            DoWin(Colors.GREEN);
        }

    }

    public void DoWin(Colors color){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Победа");
        alert.setContentText((color == Colors.BLUE ? "Синий" : "Зеленый")+" игрок победил!");
        alert.showAndWait();
        timer.stop();

    }

    public Player getPlayerWhoDoTurn(){
        return (greenPlayer.isMyTurn() ? greenPlayer : bluePlayer);
    }

    public void move(Cell oldCell){
        Player currentPlayer = getPlayerWhoDoTurn();

        getChildren().remove(oldCell);
        getChildren().remove(currentPlayer);


        System.out.print(currentPlayer.getX() + "  " + currentPlayer.getY()+"\n");
        add(currentPlayer, oldCell.getX(), oldCell.getY());
        add(oldCell, currentPlayer.getX(), currentPlayer.getY());

        int tmpX = oldCell.getX();
        int tmpY = oldCell.getY();

        oldCell.setCoords(currentPlayer);
        currentPlayer.setCoords(tmpX, tmpY);

        cells[oldCell.getIndex()] = oldCell;
        cells[currentPlayer.getIndex()] = currentPlayer;

        switchTurn();
        checkAllCells();
        setNeighborhoodsForCells();
        checkWin();
    }

    public void checkAllCells(){
        for (Cell cell : cells){
            cell.checkStyle(false);
        }
    }

    private void startTimer(){
        timer = new Timeline(
                new KeyFrame(Duration.millis(10), e -> {getPlayerWhoDoTurn().updateTimer(); setTimer();}));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void setTimer(){
        int time = getPlayerWhoDoTurn().getTimer();
        if (time < 0) {
            return;
        }
        int minutes = (time / 6000) ;
        int seconds = (time / 100)  % 60;
        int mlsec = time % 100;

        mainClass.getTimer().setText(String.format("%02d:%02d:%02d", minutes, seconds, mlsec));
    }

    public WallBuildMode getBuildMode() {
        return buildMode;
    }

    public void switchTurn(){
        greenPlayer.setMyTurn(!greenPlayer.isMyTurn());
        bluePlayer.setMyTurn(!bluePlayer.isMyTurn());
        setStatus(bluePlayer.isMyTurn() ? StatusText.TURN_BLUE : StatusText.TURN_GREEN,
                bluePlayer.isMyTurn() ? Colors.BLUE : Colors.GREEN );
        setTimer();
    }

    public void switchBuildMode() {
        checkAllCells();

        buildMode = switch (buildMode){
            case LEFT -> WallBuildMode.UP;
            case DOWN -> WallBuildMode.LEFT;
            case UP -> WallBuildMode.RIGHT;
            case RIGHT -> WallBuildMode.DOWN;
        };

        for (Cell cell: cells){
            cell.setCurrentNeighborhood();
            if (cell.isHovered()) cell.onMouseEntered();
        }


    }

    private void setNeighborhoodsForCells(){
        for (int i= 0; i<BOARD_SIZE*BOARD_SIZE; i++){
            Cell[] neighborhoodCells = new Cell[4];
            if (i - BOARD_SIZE >= 0) neighborhoodCells[0] = cells[i-BOARD_SIZE];
            else neighborhoodCells[0] = cells[i+BOARD_SIZE];

            if (i+BOARD_SIZE <= BOARD_SIZE*BOARD_SIZE-1) neighborhoodCells[1] = cells[i+BOARD_SIZE];
            else neighborhoodCells[1] = cells[i-BOARD_SIZE];

            if ((i+1) % BOARD_SIZE == 0  && i != 0 ) neighborhoodCells[2] = cells[i-1];
            else neighborhoodCells[2] = cells[i+1];

            if (i % BOARD_SIZE == 0 && i != BOARD_SIZE*BOARD_SIZE-1 || i == 0) neighborhoodCells[3] = cells[i+1];
            else neighborhoodCells[3] = cells[i-1];

            cells[i].setNeighborhoodCells(neighborhoodCells);
            cells[i].setCurrentNeighborhood();
        }
        bluePlayer.findPossibleWays();
        greenPlayer.findPossibleWays();

    }



    private int getX(int i){
        return i % BOARD_SIZE;
    }

    private int getY(int i){
        return (i-getX(i))/BOARD_SIZE;
    }

}