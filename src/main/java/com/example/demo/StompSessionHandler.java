package com.example.demo;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;


public class StompSessionHandler extends StompSessionHandlerAdapter {

    // Описываем действия с подключением к веб-сокету
    // В этом примере выводим в консоль, что подключение есть
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Opening websocket...");
        System.out.println("Session is connected: " + session.isConnected() + "\n");
    }

    // Обработчик сообщений из веб-сокета
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String json = new String((byte[]) payload);
        System.out.println("Received json:\n" + json + "\n");
    }
}

