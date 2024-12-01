package com.example.demo;

import com.example.demo.GameModels.Board;
import com.example.demo.Utilits.Helper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Designer {

    Label localStatus;

    static void createOptionMenu(){

    }

    static public BorderPane createInfoBar(Label status){
        BorderPane infoBar = new BorderPane();
        infoBar.setPrefWidth(300);
        infoBar.setPadding(new Insets(10));
        infoBar.setStyle("-fx-background-color: white; -fx-border-color:black;");
        return infoBar;
    }

    static public Label createStatus(){
        Label status = new Label();
        status.setAlignment(Pos.CENTER);
        status.setMinSize(150, 200);
        status.setMaxSize(350, 350);
        status.setFont(new Font("Cascadia Mono Regular", 25.0));
        return status;
    }

    static public Label createTimer(){
        Label timer = new Label();
        timer.setText("00:00");
        timer.setAlignment(Pos.CENTER);
        timer.setPrefSize(600, 150);
        timer.setFont(new Font("Cascadia Mono Regular", 25.0));
        return timer;
    }

    static public Background createBackground(){
        BackgroundImage backgroundImage = new BackgroundImage(Helper.loadImage("Background.png",
                1900,1900),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                null);
        return new Background(backgroundImage);
    }

    static public GridPane createOptions(Board board){
        GridPane options = new GridPane();
        options.setAlignment(Pos.CENTER);

        options.add(new OptionButton(
                        "reset.png",
                        e -> {
                            board.reload();
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

        return options;
    }
}
