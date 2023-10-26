package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//date|time|description|vendor|amount
public class Logs {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;



    public Logs (LocalDate date, LocalTime time, String description, String vendor, double amount){
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;

    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        DateTimeFormatter format= DateTimeFormatter.ofPattern("HH:mm:ss");
        time = LocalTime.parse(time.format(format));
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String type) {
        this.description = type;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return getDate()  + " | " + getTime() + " | " + getDescription() + " | "+ getVendor() +" | "+ getTime();
    }


}
