package com.example.demo.Client;

import com.example.demo.Data.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseDataDecoder
        extends ReplayingDecoder<ResponseData> {
    ObjectMapper objectMapper = new ObjectMapper();
    private final Charset charset = StandardCharsets.UTF_8;
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        int strlen = in.readInt();

        String json = in.readCharSequence(strlen, charset).toString();

        ResponseData responseData = objectMapper.readValue(json, ResponseData.class);


        out.add(responseData);

    }
}