package com.example.demo.GameModels;

import com.example.demo.*;
import com.example.demo.Data.GameDBModel;
import com.example.demo.Enums.Colors;
import com.example.demo.Enums.WallBuildMode;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.Optional;


/** Это основной класс игровой логики
 *
 * Этот класс наследуется от GrioPane,
 * в нем располагаются игровые секции (Cells),
 * происходит проверка победы и поражения и т.д.
 *
 */

public class Board extends GridPane {
    private boolean disable = false;
    private WallBuildMode buildMode = WallBuildMode.LEFT;
    public final static int BOARD_SIZE = MainClass.BOARD_SIZE;
    private int[][] matrix = new int[BOARD_SIZE][BOARD_SIZE];
    private final Cell[] cells = new Cell[BOARD_SIZE*BOARD_SIZE];
    private Player bluePlayer;
    private Player greenPlayer;
    private boolean choiced = false;
    private final GameModel gameModel;
    Timeline timer;



    public Board(GameModel gameModel){
        this.gameModel = gameModel;
        setAlignment(Pos.BOTTOM_CENTER);
        setPadding(new Insets(10));
        init(gameModel);
    }

    private void init(GameModel gameModel){
        getChildren().removeAll(getChildren());

        setMinSize(BOARD_SIZE*20, BOARD_SIZE*20);
        setMaxSize(BOARD_SIZE*50, BOARD_SIZE*50);
        bluePlayer = new Player(
                BOARD_SIZE/2,
                BOARD_SIZE-1,
                this, Colors.BLUE,
                true, (byte)0,
                gameModel

        );
        greenPlayer = new Player(
                BOARD_SIZE/2,
                0,
                this,
                Colors.GREEN,
                false, (byte)(BOARD_SIZE-1),
                gameModel
        );

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
                Cell cell = new WayCell(x, y, this, gameModel);
                add(cell, x, y);
                cells[i] = cell;
            }
        }
        setNeighborhoodsForCells();

    }

    public void reload(){
        setDisable(false);
        init(gameModel);
        setDisable(disable);
    }


    public void disableBoard(boolean disable){
        setDisable(disable);
        this.disable = disable;
    }


    /** Геттеры и сеттеры */
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

    public int[][] getMatrix() {
        return matrix;
    }

    public WallBuildMode getBuildMode() {
        return buildMode;
    }

    public void createGameFromModel(GameDBModel gameDBModel){
        getChildren().removeAll(getChildren());

        setMinSize(BOARD_SIZE*20, BOARD_SIZE*20);
        setMaxSize(BOARD_SIZE*50, BOARD_SIZE*50);
        bluePlayer = new Player(
                gameModel,
                gameDBModel.getBluePlayer(),
                Colors.BLUE,
                this
        );
        greenPlayer = new Player(
                gameModel,
                gameDBModel.getGreenPlayer(),
                Colors.GREEN,
                this
        );
        var modelMatrix = gameDBModel.getMatrix();

        add(greenPlayer, greenPlayer.getX(), greenPlayer.getY());
        add(bluePlayer, bluePlayer.getX(), bluePlayer.getY());

        int FIRST_PLAYER_POS = greenPlayer.getX()+greenPlayer.getY()*BOARD_SIZE;
        cells[FIRST_PLAYER_POS] = greenPlayer;
        int SECOND_PLAYER_POS = bluePlayer.getX()+bluePlayer.getY()*BOARD_SIZE;
        cells[SECOND_PLAYER_POS] = bluePlayer;


        for (int i= 0; i<BOARD_SIZE*BOARD_SIZE; i++){

            int x = getX(i);
            int y = getY(i);
            if (i != FIRST_PLAYER_POS && i != SECOND_PLAYER_POS) {
                WayCell cell = new WayCell(x, y, this, gameModel);
                add(cell, x, y);
                cells[i] = cell;
                if (modelMatrix[i] == 1){
                    cell.createWall();
                }
            }
        }
        setMatrix(modelMatrix);
        setNeighborhoodsForCells();

    }

    private void printMatrix() {
        for (var el: matrix) System.out.println(Arrays.toString(el));
    }


    /** Изменение матрицы
     *
     * @param x - Х-координата
     * @param y - У-координата
     * @param value - Значение, на которое меняем
     */

    public void changeMatrix(int x, int y, int value){
        matrix[y][x] = value;
    }

    /** Произведение хода игрока
     *
     * @param x
     * @param player
     * @param y
     * */
    public void move(int x, int y, Player player){
        Optional<Cell> OptionalfindedCell = findCell(x,y);

        if (OptionalfindedCell.isEmpty()) return;
        Cell findedCell = OptionalfindedCell.get();

        getChildren().remove(findedCell);
        getChildren().remove(player);

        add(player, x, y);
        add(findedCell, player.getX(), player.getY());

        findedCell.setCoords(player);
        player.setCoords(x, y);

        cells[findedCell.getIndex()] = findedCell;
        cells[player.getIndex()] = player;

        checkAllCells();
        setNeighborhoodsForCells();
    }


    /** Проверка статуса всех клеток (Глобальное обновление экрана)*/
    public void checkAllCells(){
        for (Cell cell : cells){
            cell.checkStyle(false);
        }
    }

    private Optional<Cell> findCell(int x, int y){
        for (Cell cell : cells){
            if (cell.getX() == x && cell.getY() == y) return Optional.of(cell);
        }
        return Optional.empty();
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

    public void setMatrix(int[] matrix) {

        // Переменная для отслеживания индекса в одномерном массиве
        int index = 0;

        // Заполняем двумерную матрицу значениями из одномерного массива
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.matrix[i][j] = matrix[index++];
            }
        }
    }

    private int getX(int i){
        return i % BOARD_SIZE;
    }

    private int getY(int i){
        return (i-getX(i))/BOARD_SIZE;
    }

    public void createWalls(int x1, int y1, int x2, int y2) {
        changeMatrix(x1, y1, 1);
        changeMatrix(x2,y2, 1);
        gameModel.getPlayerWhoDoTurn().useWall();

        Optional<Cell> OptionalfindedCell = findCell(x1,y1);
        if (OptionalfindedCell.isEmpty()) return;
        ((WayCell) OptionalfindedCell.get()).createWall();

        OptionalfindedCell = findCell(x2,y2);
        if (OptionalfindedCell.isEmpty()) return;
        ((WayCell) OptionalfindedCell.get()).createWall();

        checkAllCells();
    }

}
