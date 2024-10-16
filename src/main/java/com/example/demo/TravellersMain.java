package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TravellersMain extends Application {
    public final static int BOARD_SIZE = 15;
    private Board board;
    private Label status;



    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Chess");

        //main BorderPane
        BorderPane pane = new BorderPane();

        //chess board with column and row markings
        GridPane table = new GridPane();
        for (int i = 0; i < BOARD_SIZE; i++) {
            table.add(newRowLabel(i), 0, i + 1, 1, 1);
            table.add(newRowLabel(i), BOARD_SIZE+1, i + 1, 1, 1);
            table.add(newColLabel(i), i + 1, 0, 1, 1);
            table.add(newColLabel(i), i + 1, BOARD_SIZE+1, 1, 1);
        }

        table.add(board = new Board(), 1, 1, BOARD_SIZE, BOARD_SIZE);


        table.setAlignment(Pos.CENTER);
        pane.setCenter(table);

        GridPane options = new GridPane();
        options.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane menu = new BorderPane();
        menu.setPadding(new Insets(10, 10, 10, 0));


        options.add(new OptionButton(
                        "reset.png",
                        e -> {

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
        status.setAlignment(Pos.BOTTOM_LEFT);
        status.setPadding(new Insets(10, 0, 10, 10));
        menu.setLeft(status);

        pane.setBottom(menu);

        Scene scene = new Scene(pane, 880, 680);
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
        l.setAlignment(Pos.CENTER);
        return l;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
