package com.example.easybooking.models;

import java.math.BigDecimal;

public class Hotel {
    private String hotelId;
    private String hotelImageUrl;
    private String name;
    private String description;
    private String location;
    private double latitude;
    private double longitude;
    private double rating;
    private double pricePerNight;
    private double pricePerHour;

    public Hotel() {
    }

    public Hotel(String hotelId, String hotelImageUrl, String name, String description, String location,
                 double latitude, double longitude, double rating, double pricePerNight, double pricePerHour) {
        this.hotelId = hotelId;
        this.hotelImageUrl = hotelImageUrl;
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
        this.pricePerHour = pricePerHour;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelImageUrl() {
        return hotelImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }
}
