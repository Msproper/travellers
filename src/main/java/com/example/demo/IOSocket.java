package com.example.demo;

import com.example.demo.Data.RequestData;
import com.example.demo.GameModels.GameModel;

public interface IOSocket {
    public void run(GameModel gameModel) throws Exception;

    public void sendMessage(RequestData requestData);
}
