package com.example.demo.Data;

import jakarta.persistence.*;

@Entity
public class GameDBModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name="name")
    private String name;

    @Column
    private int boardSize;

    @Column(name="Matrix")
    private int[] matrix;

    @OneToOne
    @JoinColumn(name="BluePlayerId")
    private PlayerDTO bluePlayer;

    @OneToOne
    @JoinColumn(name="GreenPlayerId")
    private PlayerDTO greenPlayer;

    @Column(name="MyMove")
    private boolean myMove;

    public Long getId() {
        return id;
    }

    public boolean isMyMove() {
        return myMove;
    }

    public void setMyMove(boolean myMove) {
        this.myMove = myMove;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int[] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
    }

    public PlayerDTO getBluePlayer() {
        return bluePlayer;
    }

    public void setBluePlayer(PlayerDTO bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public PlayerDTO getGreenPlayer() {
        return greenPlayer;
    }

    public void setGreenPlayer(PlayerDTO greenPlayer) {
        this.greenPlayer = greenPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Геттеры и сеттер
}
