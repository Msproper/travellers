package com.example.demo;


import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WSClient {
    public void firstWSClient() {

        // Инициализируем классы библиотеки
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler myStompSessionHandler = new StompSessionHandler();

        // Определяем заголовки при обновлении http до ws (handshake)
        WebSocketHttpHeaders websocketheaders = new WebSocketHttpHeaders();
        // Если требуется передавать авторизационные данные (токен или пароль)

        // Определяем заголовок сообщений, передаваемых по веб-сокету
        StompHeaders stompHeaders = new StompHeaders();
        // Например, указываем топик, на который хотим подписаться
        stompHeaders.set(StompHeaders.DESTINATION, "topic/public");

        // Указываем URL веб-сокета, к которому требуется подключиться
        String URL = "ws://localhost:8080/websocket";

        // Подключение к веб-сокету
        ListenableFuture<StompSession> sessionAsync = stompClient.connect(URL, websocketheaders, stompHeaders, myStompSessionHandler);
        StompSession stompSession = null;
        try {
            stompSession = sessionAsync.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // Подписываемся на топик
        //stompSession.subscribe(stompHeaders, myStompSessionHandler);

        // Оправка сообщения по веб-сокету на сервер
        //stompSession.send(stompHeaders, "Hello, World!");
    }
}
