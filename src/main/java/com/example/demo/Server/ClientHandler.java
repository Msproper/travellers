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
        if (gameModel.isTurn()) gameModel.startBoard();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RequestData coords = (RequestData)msg;
        Platform.runLater(() -> gameModel.doMove(coords.getMoveX(), coords.getMoveY()));


    }
}
