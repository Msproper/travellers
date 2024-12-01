package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestData msg, ByteBuf out) throws Exception {
        String json = objectMapper.writeValueAsString(msg);
        System.out.println("Send!");
        out.writeInt(json.length());
        out.writeCharSequence(json, charset);

    }
}