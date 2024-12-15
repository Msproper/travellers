package com.example.demo.Data;
import jakarta.persistence.*;


@Entity
public class PlayerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private boolean myTurn;

    @Column
    private byte winRow;

    @Column
    private int numberOfWalls;

    @Column
    private int timer;

    @Column(name="x-coord")
    private int x;

    @Column(name="y-coord")
    private int y;

    public PlayerDTO() {
    }

    /**Конструктор класса
     *
     * @param myTurn чей ход
     * @param winRow победная строка
     * @param numberOfWalls количество доступных стен
     * @param timer таймер
     */
    public PlayerDTO(int x, int y, boolean myTurn, byte winRow, int numberOfWalls, int timer) {
        this.myTurn = myTurn;
        this.winRow = winRow;
        this.numberOfWalls = numberOfWalls;
        this.timer = timer;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
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

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public byte getWinRow() {
        return winRow;
    }

    public void setWinRow(byte winRow) {
        this.winRow = winRow;
    }

    public int getNumberOfWalls() {
        return numberOfWalls;
    }

    public void setNumberOfWalls(int numberOfWalls) {
        this.numberOfWalls = numberOfWalls;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id=" + id +
                ", myTurn=" + myTurn +
                ", winRow=" + winRow +
                ", numberOfWalls=" + numberOfWalls +
                ", timer=" + timer +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
