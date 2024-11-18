package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import com.example.demo.Data.ResponseData;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        RequestData requestData = (RequestData) msg;
        ResponseData responseData = new ResponseData();
        responseData.setIntValue(requestData.getIntValue() * 2);
        ChannelFuture future = ctx.writeAndFlush(responseData);
        System.out.println("I have request "+ requestData);
    }
}