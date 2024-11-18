package com.example.demo.Data;

public class RequestData {
    private int intValue;
    private String stringValue;

    public RequestData() {
    }

    public RequestData(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "intValue=" + intValue +
                ", stringValue='" + stringValue + '\'' +
                '}';
    }
    // standard getters and setters
}