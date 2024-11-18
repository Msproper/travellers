package com.example.demo.Client;

import com.example.demo.Data.ResponseData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    int a = 0;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

      //todo
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Message arrived!: " + ((ResponseData)msg).getIntValue());

    }
}
