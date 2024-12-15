package com.example.demo.GameModels;

import com.example.demo.Data.GameDBModel;
import com.example.demo.Data.PlayerDTO;
import com.example.demo.Data.RequestData;
import com.example.demo.Enums.Colors;
import com.example.demo.Enums.DataType;
import com.example.demo.Enums.StatusText;
import com.example.demo.HibernateUtil;
import com.example.demo.IOSocket;
import com.example.demo.MainClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class GameModel {

    public IOSocket socket;
    private final MainClass mainClass;
    private Player bluePlayer;
    private Player greenPlayer;
    private final Label timerLabel;
    private Timeline timeline;
    private Board board;
    private final Label wallsOfBlue;
    private final Label wallsOfGreen;

    private boolean turn = false;


    public GameModel(MainClass mainClass, Label timerLabel, Label wallsOfBlue, Label wallsOfGreen) throws InterruptedException {
        this.mainClass = mainClass;
        this.timerLabel = timerLabel;
        this.wallsOfBlue = wallsOfBlue;
        this.wallsOfGreen = wallsOfGreen;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
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
        wallsOfBlue.setText(String.valueOf(bluePlayer.getNumberOfWalls()));
        wallsOfGreen.setText(String.valueOf((greenPlayer.getNumberOfWalls())));
    }

    /**Проверка возможных ходов для обоих игроков*/
    public void checkPossibleWaysForBoth(){
        greenPlayer.findPossibleWays();
        bluePlayer.findPossibleWays();
    }


    public void sendMessage(RequestData requestData){
        if (socket != null){
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

    public Label getWallsOfBlue() {
        return wallsOfBlue;
    }

    public Label getWallsOfGreen() {
        return wallsOfGreen;
    }

    private void setTimer(){
        int time = getPlayerWhoDoTurn().getTimer();
        if (time < 0) {
            return;
        }
        int minutes = (time / 6000) ;
        int seconds = (time / 100)  % 60;
        int mlsec = time % 100;

        timerLabel.setText(String.format("%02d:%02d:%02d", minutes, seconds, mlsec));
    }

    /** Процесс показа окна подтверждения или отказа от перезапуска игры
     *
     * @param turn  обозначает, чей будет ход, если перезапуск одобрен
     */
    public void incomingVoteToReload(boolean turn){
        pauseGame();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Перезапуск");
        alert.setHeaderText(null);
        alert.setContentText("Игрок просит перезапустить игру. Вы согласны?");

        ButtonType choiceYes = new ButtonType("Да");
        ButtonType choiceNo = new ButtonType("Нет");

        alert.getButtonTypes().setAll(choiceNo, choiceYes);

        Optional<ButtonType> result = alert.showAndWait();
        RequestData requestData = new RequestData(DataType.RELOAD_GAME, null);
        if (result.get() == choiceNo){
            requestData.setInfo("REJECT");
        } else if (result.get() == choiceYes) {
            restartGame(turn);
            requestData.setInfo("APPROVE");
        } else {
            return;
        }
        sendMessage(requestData);
        continueGame();
    }

    public void loadGame(GameDBModel game, boolean turn){
        continueGame();
        if (game != null) {
            board.createGameFromModel(game);
            setPlayer(board.getBluePlayer(), bluePlayer.board.getGreenPlayer());
            this.turn = turn;
            setTimer();
            if(turn)startBoard();
            else stopBoard();
        }
    }

    public void saveGameInDB(){
        LocalDateTime now = LocalDateTime.now();

        // Форматирование даты и времени
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        GameDBModel gameDBModel = new GameDBModel();
        gameDBModel.setMyMove(turn);
        gameDBModel.setMatrix(refactorMatrix(board.getMatrix()));
        gameDBModel.setBoardSize(MainClass.BOARD_SIZE);

        PlayerDTO bluePlayerDTO = bluePlayer.getPlayerDTOFromPlayer();
        session.save(bluePlayerDTO);

        PlayerDTO greenPlayerDTO = greenPlayer.getPlayerDTOFromPlayer();
        session.save(greenPlayerDTO);

        gameDBModel.setBluePlayer(bluePlayerDTO);
        gameDBModel.setGreenPlayer(greenPlayerDTO);
        gameDBModel.setName(formattedDate);

        session.save(gameDBModel);
        transaction.commit();
        session.close();

    }

    public void restartGame(boolean turn){
        board.reload();
        bluePlayer = board.getBluePlayer();
        greenPlayer = board.getGreenPlayer();
        this.turn = turn;
        setStatus(bluePlayer.isMyTurn() ? StatusText.TURN_BLUE : StatusText.TURN_GREEN,
                bluePlayer.isMyTurn() ? Colors.BLUE : Colors.GREEN );
        setTimer();
        if(turn)startBoard();
        else stopBoard();
    }

    public void voteToReload(){
        pauseGame();
        sendMessage(new RequestData(DataType.RELOAD_GAME, "INVITE"));
    }

    public void voteToUpload(){
        pauseGame();
        Session session = HibernateUtil.getSessionFactory().openSession();
        GameDBModel game = (GameDBModel) session.createQuery("FROM GameDBModel ORDER BY id DESC")
                .setMaxResults(1)
                .uniqueResult();
        sendMessage(new RequestData(DataType.UPLOAD_GAME, game, "INVITE"));
        session.close();
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

    public void initializeGame(){
        if (turn) startBoard();
        startTimer();
    }

    /** Метод для осуществения хода игрока для перемещения
     *
     * @param x Х-координата клетки
     * @param y Y-координата клетки
     */
    public void doMove(int x, int y){
        if (turn) {
            turn = false;
            stopBoard();
            sendMessage(new RequestData(DataType.PLAYER_MOVE, x, y ));
        }
        else {
            startBoard();
            //getPlayerWhoDoTurn().useWall();
            turn = true;
        }
        board.move(x, y, getPlayerWhoDoTurn());
        checkWin();
        switchTurn();
    }

    public void doMove(int x1, int y1, int x2, int y2){
        if (turn) {
            turn = false;
            stopBoard();
            sendMessage(new RequestData(DataType.SET_WALL, x1, y1, x2, y2));
        }
        else {
            startBoard();
            turn = true;
            board.createWalls(x1, y1, x2, y2);
        }

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

    public void continueGame() {
        mainClass.continueWindow();
    }

    public void pauseGame(){
        mainClass.pauseWindow();
    }

    public void closeResources(){
        socket.close();
        HibernateUtil.closeSessionFactory();
    }

    private int[] refactorMatrix(int[][] matrix){
        int[] vector = new int[MainClass.BOARD_SIZE*MainClass.BOARD_SIZE];
        int index = 0;
        for (int[] innerArray : matrix) {
            System.out.println(Arrays.toString(innerArray));
            System.arraycopy(innerArray, 0, vector, index, innerArray.length);
            index += innerArray.length;
        }
        return vector;
    }

    public void incomingVoteToUpload(GameDBModel gameDBModel){
        pauseGame();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Запуск сохраненной игры");
        alert.setHeaderText(null);
        alert.setContentText("Игрок предлагает запустить последнее его сохранение. Вы согласны?");

        ButtonType choiceYes = new ButtonType("Да");
        ButtonType choiceNo = new ButtonType("Нет");

        alert.getButtonTypes().setAll(choiceNo, choiceYes);

        Optional<ButtonType> result = alert.showAndWait();
        RequestData requestData = new RequestData(DataType.UPLOAD_GAME, null);
        if (result.get() == choiceNo){
            requestData.setInfo("REJECT");
            continueGame();
        } else if (result.get() == choiceYes) {
            loadGame(gameDBModel, !gameDBModel.isMyMove());
            requestData.setInfo("APPROVE");
            requestData.setGameDBModel(gameDBModel);
            continueGame();
        } else {
            return;
        }
        sendMessage(requestData);

    }
}
