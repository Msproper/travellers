package com.example.demo;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Helper {

    public static Image loadImage(String resource, int width, int height) {
        return new Image(Helper.class.getClassLoader().getResourceAsStream(resource), width, height, true, true);
    }


    public static void saveDataToFile(byte[] data, File file) {
        try {
            Files.write(Paths.get(file.toURI()), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

