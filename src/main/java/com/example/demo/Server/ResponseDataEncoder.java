package com.example.demo.Server;

import com.example.demo.Data.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResponseDataEncoder
        extends MessageToByteEncoder<ResponseData> {

    ObjectMapper objectMapper = new ObjectMapper();
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResponseData msg, ByteBuf out) throws Exception {

        String json = objectMapper.writeValueAsString(msg);
        System.out.println("I create new response data " + json);
        out.writeInt(json.length());
        out.writeCharSequence(json, charset);
    }
}