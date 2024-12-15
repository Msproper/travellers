package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import com.example.demo.GameModels.GameModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    GameModel gameModel;

    public ClientHandler(GameModel gameModel) {
        super();
        this.gameModel = gameModel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Platform.runLater(()->gameModel.initializeGame());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RequestData requestData= (RequestData) msg;
        switch (requestData.getDataType()){
            case PLAYER_MOVE -> Platform.runLater(() -> gameModel.doMove(requestData.getX(), requestData.getY()));
            case SET_WALL -> Platform.runLater(() -> gameModel.doMove(requestData.getX(), requestData.getY(), requestData.getX2(), requestData.getY2()));
            case RELOAD_GAME -> {
                switch (requestData.getInfo()){
                    case "INVITE" -> Platform.runLater(() -> gameModel.incomingVoteToReload(false));

                    case "APPROVE" -> Platform.runLater(()-> gameModel.restartGame(false));

                    case "REJECT" -> Platform.runLater(()->gameModel.continueGame());

                }

            }
            case UPLOAD_GAME -> {
                switch (requestData.getInfo()){
                    case "INVITE" -> Platform.runLater(() -> gameModel.incomingVoteToUpload(requestData.getGameDBModel()));

                    case "APPROVE" -> Platform.runLater(()-> gameModel.loadGame(requestData.getGameDBModel(), requestData.getGameDBModel().isMyMove()));

                    case "REJECT" -> Platform.runLater(()->gameModel.continueGame());

                }

            }
            case null, default -> {
                System.out.println(
                        "Found untyped data"
                );
            }
        }

    }
}
