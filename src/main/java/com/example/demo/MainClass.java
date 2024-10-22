package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainClass extends Application {
    public final static int BOARD_SIZE = 10;
    private Board board;
    private Label status;
    private MainClass instance;

    public Label getStatus() {
        return status;
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primaryStage.setTitle("Travellers");

        //main BorderPane
        BorderPane pane = new BorderPane();

        GridPane options = new GridPane();
        options.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane menu = new BorderPane();
        menu.setPadding(new Insets(10, 10, 10, 0));


        options.add(new OptionButton(
                        "reset.png",
                        e -> {

                            board = new Board(this);
                            board.setAlignment(Pos.CENTER);
                            board.setPadding(new Insets(10, 10, 10, 10));
                            pane.setLeft(board);
                        },
                        "Reset"),
                0, 0, 1, 1
        );
        options.add(new OptionButton(
                        "save.png",
                        e -> {
                        },
                        "Save"),
                1, 0, 1, 1
        );
        menu.setRight(options);


        status = new Label();
        status.setAlignment(Pos.CENTER);
        status.setPadding(new Insets(10, 0, 10, 10));
        status.setPrefSize(200, 200);
        status.setFont(new Font("Cascadia Mono Regular", 20.0));
        menu.setCenter(status);

        pane.setRight(menu);

        board = new Board(this);
        board.setAlignment(Pos.BOTTOM_CENTER);
        board.setPadding(new Insets(10, 10, 10, 10));
        pane.setCenter(board);

        Scene scene = new Scene(pane, 1080, 880);
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

    private Label newRowLabel(int i) {
        Label l = new Label(BOARD_SIZE - i + "");
        l.setMinSize(10, 25);
        l.setMaxSize(50, 75);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private Label newColLabel(int i) {
        Label l = new Label(BOARD_SIZE-i + "");
        l.setMinSize(25, 10);
        l.setMaxSize(75, 50);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
