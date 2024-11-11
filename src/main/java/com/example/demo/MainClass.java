package com.example.demo;

import com.example.demo.GameModels.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainClass extends Application {



    public final static int BOARD_SIZE = 10;
    private Board board;
    private Label status;
    private MainClass instance;
    private Label timer;

    public Label getStatus() {
        return status;
    }

    public Label getTimer() {return timer;}

    @Override
    public void start(Stage primaryStage){

        //WSClient wsClient = new WSClient();
        //wsClient.firstWSClient();

        instance = this;
        primaryStage.setTitle("Travellers");

        //main BorderPane
        BorderPane pane = new BorderPane();

        GridPane options = new GridPane();
        options.setAlignment(Pos.CENTER);



        options.add(new OptionButton(
                        "reset.png",
                        e -> {

                            board = new Board(this);
                            board.setAlignment(Pos.CENTER);
                            board.setPadding(new Insets(10));
                            pane.setCenter(board);
                        },
                        "Reset"),
                0, 0, 2, 2
        );
        options.add(new OptionButton(
                        "save.png",
                        e -> {
                        },
                        "Save"),
                2, 0, 2, 2
        );

        createStatus();

        createTimer();

        BorderPane infoBar = createInfoBar();

        infoBar.setCenter(options);

        infoBar.setBottom(timer);


        pane.setRight(infoBar);


        board = new Board(this);
        board.setAlignment(Pos.BOTTOM_CENTER);
        board.setPadding(new Insets(10));
        pane.setCenter(board);


        BackgroundImage backgroundImage = new BackgroundImage(Helper.loadImage("Background.png",
                1900,1900),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                null);
        pane.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(pane, 1580, 880);

        scene.setOnKeyPressed((KeyEvent)-> {
            if (KeyCode.SPACE == KeyEvent.getCode()){
                board.switchBuildMode();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    private BorderPane createInfoBar(){
        BorderPane infoBar = new BorderPane();
        infoBar.setTop(status);
        infoBar.setPrefWidth(300);
        infoBar.setPadding(new Insets(10));
        infoBar.setStyle("-fx-background-color: white; -fx-border-color:black;");
        return infoBar;
    }

    private void createStatus(){
        status = new Label();
        status.setAlignment(Pos.CENTER);
        status.setMinSize(150, 200);
        status.setMaxSize(350, 350);
        status.setFont(new Font("Cascadia Mono Regular", 25.0));
    }

    private void createTimer(){
        timer = new Label();
        timer.setText("00:00");
        timer.setAlignment(Pos.CENTER);
        timer.setPrefSize(600, 150);
        timer.setFont(new Font("Cascadia Mono Regular", 25.0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
