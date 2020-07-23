package com.example.btest.wheelpressureapp;

//Clasa ce tine evidenta parametrilor
public class SensorAtributes {

    private String hpa;
    private String temp;
    private String bat;
    private String procent;
    private String voltage;


    public SensorAtributes() {
    }

    public String getHpa() {
        return hpa;
    }

    public void setHpa(String hpa) {
        this.hpa = hpa;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getBat() {
        return bat;
    }

    public void setBat(String bat) {
        this.bat = bat;
    }

    public String getProcent() {
        return procent;
    }

    public void setProcent(String procent) {
        this.procent = procent;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }
}
