module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires spring.websocket;
    requires spring.messaging;
    requires spring.core;
    requires spring.webflux;
    requires reactor.core;
    requires javax.websocket.api;
    requires org.json;
    requires io.netty.codec;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires com.fasterxml.jackson.databind;
    requires io.netty.common;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens com.example.demo.Data;
    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.GameModels;
    opens com.example.demo.GameModels to javafx.fxml;
    exports com.example.demo.Enums;
    opens com.example.demo.Enums to javafx.fxml;
    exports com.example.demo.Utilits;
    opens com.example.demo.Utilits to javafx.fxml;
    exports com.example.demo.Server;
    opens com.example.demo.Server to javafx.fxml;
    exports com.example.demo.Data;
    exports com.example.demo.Component;
    opens com.example.demo.Component to javafx.fxml;
}