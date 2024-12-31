package com.example.easybooking.models;

import java.math.BigDecimal;

public class Car {
    private String carId;
    private String carImageUrl;
    private String name;
    private String brand;
    private String model;
    private String description;
    private BigDecimal pricePerDay;
    private BigDecimal pricePerHour;

    public Car() {
    }

    public Car(String carId, String carImageUrl, String name, String brand, String model, String description,
               BigDecimal pricePerDay, BigDecimal pricePerHour) {
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

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }
}
