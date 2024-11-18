package com.example.demo;

import com.example.demo.GameModels.Board;
import com.example.demo.Utilits.Helper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;


public class MainClass extends Application {



    public final static int BOARD_SIZE = 10;
    private Board board;
    private Label status;
    private Label timer;



    public Label getStatus() {
        return status;
    }

    public Label getTimer() {return timer;}

    @Override
    public void start(Stage primaryStage){


//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Режим игры");
//        alert.setHeaderText(null);
//        alert.setContentText("Выберите режими игры");
//
//        ButtonType choiceHost = new ButtonType("Host");
//        ButtonType choiceClient = new ButtonType("Cancel");
//
//        alert.getButtonTypes().setAll(choiceClient, choiceHost);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == choiceClient){
//            // ... user chose "One"
//        } else if (result.get() == choiceHost) {
//            // ... user chose "Two"
//        } else {
//            return;
//        }

        status = Designer.createStatus();
        timer = Designer.createTimer();

        board = new Board(this);


        BorderPane pane = new BorderPane();
        GridPane options = Designer.createOptions(board);


        BorderPane infoBar = Designer.createInfoBar(status);

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

        primaryStage.setTitle("Travellers");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }



    public static void main(String[] args) {
        launch(args);
    }
}
