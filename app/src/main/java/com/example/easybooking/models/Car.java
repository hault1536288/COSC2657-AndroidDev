package com.example.easybooking.models;

import java.math.BigDecimal;

public class Car {
    private String carId;
    private String carImageUrl;
    private String name;
    private String brand;
    private String model;
    private String description;
    private double pricePerDay;
    private double pricePerHour;

    public Car() {
    }

    public Car(String carId, String carImageUrl, String name, String brand, String model, String description,
               double pricePerDay, double pricePerHour) {
        this.carId = carId;
        this.carImageUrl = carImageUrl;
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
    }

    public String getCarId() {
        return carId;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }


    public String getName() {
        return name;
    }


    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}
