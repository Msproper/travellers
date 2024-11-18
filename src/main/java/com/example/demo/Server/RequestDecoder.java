package com.example.demo.Server;

import com.example.demo.Data.RequestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestData> {

    private final Charset charset = StandardCharsets.UTF_8;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {
        int strLen = in.readInt();
        RequestData requestData = objectMapper.readValue(in.readCharSequence(strLen, charset).toString(), RequestData.class);
        out.add(requestData);
    }
}
