package com.example.demo.GameModels;

import com.example.demo.Data.RequestData;
import com.example.demo.Enums.Colors;
import com.example.demo.Enums.StatusText;
import com.example.demo.IOSocket;
import com.example.demo.MainClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameModel {

    private boolean start = false;
    public IOSocket socket;
    private final MainClass mainClass;
    private Player bluePlayer;
    private Player greenPlayer;
    private Label timerLabel;
    private Timeline timeline;
    private Board board;

    private boolean turn = false;


    public GameModel(MainClass mainClass, Label timerLabel) throws InterruptedException {
        this.mainClass = mainClass;
        this.timerLabel = timerLabel;
        //startTimer();
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setBoard(Board board) {
        this.board = board;
        setPlayer(board.getBluePlayer(), board.getGreenPlayer());
    }

    public void setPlayer(Player bluePlayer, Player greenPlayer){
        this.bluePlayer = bluePlayer;
        this.greenPlayer = greenPlayer;
        setStatus(bluePlayer.isMyTurn() ? StatusText.TURN_BLUE : StatusText.TURN_GREEN,
                bluePlayer.isMyTurn() ? Colors.BLUE : Colors.GREEN );
    }

    /**Проверка возможных ходов для обоих игроков*/
    public void checkPossibleWaysForBoth(){
        greenPlayer.findPossibleWays();
        bluePlayer.findPossibleWays();
    }


    public void sendMessage(RequestData requestData){
        if (socket != null){
            System.out.println("Suck");
            socket.sendMessage(requestData);
        }
    }

    public void stopBoard(){
        board.disableBoard(true);
    }

    public void startBoard(){
        board.disableBoard(false);
    }

    public void setStatus(StatusText statusText, Colors color){
        mainClass.getStatus().setText(statusText.getDescription());
        mainClass.getStatus().setStyle("-fx-text-fill: "+color.getDescription() + "-fx-background-color: #AACC99;");

    }
    /** Проверка победы / поражения */
    public void checkWin(){
        if (bluePlayer.isWin() || greenPlayer.isLose()){
            doWin(Colors.BLUE);
            return;
        }
        if (greenPlayer.isWin() || bluePlayer.isLose()){
            doWin(Colors.GREEN);
        }

    }

    /** Объявление победы
     *
     * @param color - цвет игрока победителя
     */
    public void doWin(Colors color){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Победа");
        alert.setContentText((color == Colors.BLUE ? "Синий" : "Зеленый")+" игрок победил!");
        alert.showAndWait();
        timeline.stop();
        //setDisable(true);
    }

    private void startTimer(){
        if (timeline != null) timeline.stop();
        timeline = new Timeline(
                new KeyFrame(Duration.millis(10), e -> {getPlayerWhoDoTurn().updateTimer(); setTimer();}));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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


    public Player getPlayerWhoDoTurn(){
        return (greenPlayer.isMyTurn() ? greenPlayer : bluePlayer);
    }

    public void switchTurn(){
        greenPlayer.setMyTurn(!greenPlayer.isMyTurn());
        bluePlayer.setMyTurn(!bluePlayer.isMyTurn());
        setStatus(bluePlayer.isMyTurn() ? StatusText.TURN_BLUE : StatusText.TURN_GREEN,
                bluePlayer.isMyTurn() ? Colors.BLUE : Colors.GREEN );
        setTimer();
    }

    public void doMove(int x, int y){
        if (turn) {
            turn = false;
            stopBoard();
            sendMessage(new RequestData(x, y));
        }
        else {
            startBoard();
            turn = true;
        }
        board.move(x, y, getPlayerWhoDoTurn());
        checkWin();
        switchTurn();
    }

    public void setSocket(IOSocket socket) {
        this.socket = socket;
        try {
            stopBoard();
            socket.run(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
