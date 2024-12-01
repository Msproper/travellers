package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import com.example.demo.GameModels.GameModel;
import com.example.demo.IOSocket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer implements IOSocket {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    private int port;
    ChannelFuture f;
    ProcessingHandler processingHandler;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run(GameModel gameModel) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch){
                        ch.pipeline().addLast(new RequestDataEncoder(),
                                new RequestDecoder(),
                                processingHandler = new ProcessingHandler(gameModel));
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {
            f = b.bind(port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(RequestData requestData) {
        processingHandler.sendMessage(requestData);
    }

    private void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}