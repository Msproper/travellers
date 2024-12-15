package com.example.demo;

import com.example.demo.Component.Designer;
import com.example.demo.Server.NettyClient;
import com.example.demo.GameModels.Board;
import com.example.demo.GameModels.GameModel;
import com.example.demo.Server.NettyServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;


public class MainClass extends Application {



    public final static int BOARD_SIZE = 10;
    private Board board;
    private Label status;
    private Label timer;
    private BorderPane pane;
    private GameModel gameModel;
    //private Runnable socket;
    private final String HOST = "localhost";
    private final int PORT = 7777;
    String name;




    public Label getStatus() {
        return status;
    }

    public Label getTimer() {return timer;}

    @Override
    public void start(Stage primaryStage) throws Exception {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Режим игры");
        alert.setHeaderText(null);
        alert.setContentText("Выберите режими игры");

        ButtonType choiceHost = new ButtonType("Host");
        ButtonType choiceClient = new ButtonType("Client");

        alert.getButtonTypes().setAll(choiceClient, choiceHost);

        Optional<ButtonType> result = alert.showAndWait();
        IOSocket socket;
        if (result.get() == choiceClient){
            socket = new NettyClient(HOST, PORT);
            name = "Clients";
        } else if (result.get() == choiceHost) {
            name = "Server";
            socket = new NettyServer(PORT);
        } else {
            return;
        }

        Label wallsOfBlue = Designer.createStatus();
        Label wallsOfGreen = Designer.createStatus();

        wallsOfBlue.setText("8");
        wallsOfGreen.setText("8");
        status = Designer.createStatus();
        timer = Designer.createTimer();
        gameModel = new GameModel(this, timer,wallsOfBlue, wallsOfGreen);
        gameModel.setTurn(name.equals("Server"));
        board = new Board(gameModel);
        gameModel.setBoard(board);


        gameModel.setSocket(socket);

        pane = new BorderPane();
        GridPane options = Designer.createOptions(gameModel);

        BorderPane infoBar = Designer.createInfoBar(status);



        options.add(wallsOfBlue, 0, 2, 1, 1);
        options.add(wallsOfGreen, 2, 2, 1, 1);

        infoBar.setTop(status);
        infoBar.setCenter(options);
        infoBar.setBottom(timer);

        pane.setRight(infoBar);
        pane.setCenter(board);
        pane.setBackground(Designer.createBackground());

        Scene scene = new Scene(pane, 1580, 880);

        scene.setOnKeyPressed((KeyEvent)-> {
            if (KeyCode.SPACE == KeyEvent.getCode()){
                board.switchBuildMode();
            }
        });

        primaryStage.setTitle(name);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setOnCloseRequest(e->gameModel.closeResources());
    }

    public void pauseWindow() {
        pane.setDisable(true);
    }
    public void continueWindow() {
        pane.setDisable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
