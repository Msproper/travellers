package com.example.demo.Client;

import com.example.demo.Data.RequestData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {



    private final String host;
    private final int port;
    private Channel channel;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {




 //       try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new RequestDataEncoder(),
                            new ResponseDataDecoder(), new ClientHandler());
                }
            });

            channel = b.connect(host, port).sync().channel();



//            channel.closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
    }

    public void sendToServer(RequestData requestData){
        channel.writeAndFlush(requestData);
    }

    public void close(){

        channel.close();
        workerGroup.shutdownGracefully();
    }
}