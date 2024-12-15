package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import com.example.demo.GameModels.GameModel;
import com.example.demo.IOSocket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.ConnectException;

public class NettyClient implements IOSocket {



    private final String host;
    private final int port;
    private Channel channel;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run(GameModel gameModel) {

            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new RequestDataEncoder(),
                            new RequestDecoder(), new ClientHandler(gameModel));
                }
            });

        int maxAttempts = 30; // Максимальное количество попыток
        int attemptDelay = 2000; // Задержка между попытками в миллисекундах (3 секунды)

        for (int i = 1; i < maxAttempts+1; i++) {
            try {
                channel = b.connect(host, port).sync().channel();
                System.out.println("Соединение установлено успешно!");
                break; // Выход из цикла если соединение установлено
            } catch (Exception e) {
                System.out.printf("Попытка %d: Не удалось подключиться к серверу%n", i);
                if (i == maxAttempts) {
                    System.out.println("Все попытки исчерпаны. Сервер не найден.");
                } else {
                    try {
                        Thread.sleep(attemptDelay); // Ожидание перед следующей попыткой
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt(); // Перехватываем InterruptedException
                    }
                }
            }
        }



    }

    public void sendMessage(RequestData requestData){
        channel.writeAndFlush(requestData);
    }

    public void close(){
        channel.close();
        workerGroup.shutdownGracefully();
    }
}