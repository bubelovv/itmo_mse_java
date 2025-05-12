package ru.itmo;

import java.util.Date;

public class Car {
    private long id;
    private String make;
    private String model;
    private String color;
    private Date manufactureDate;
    private Double price;

    public Car() {
    }

    public Car(long id, String make, String model, String color, Date manufactureDate, Double price) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.manufactureDate = manufactureDate;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
