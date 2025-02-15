package com.example.easybooking.models;

import java.math.BigDecimal;
import java.util.Date;

public class Transport {
    private String transportId;
    private String type;
    private String brand;
    private double price;
    private Date departureDate;
    private Date arrivalDate;
    private String departureLocation;
    private String arrivalLocation;
    private double departureLatitude;
    private double departureLongitude;
    private double arrivalLatitude;
    private double arrivalLongitude;
    public Transport() {}
    public Transport(String transportId, String type, String brand, double price, Date departureDate,
                     Date arrivalDate, String departureLocation, String arrivalLocation, double departureLatitude,
                     double departureLongitude, double arrivalLatitude, double arrivalLongitude) {
        this.transportId = transportId;
        this.type = type;
        this.brand = brand;
        this.price = price;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureLatitude = departureLatitude;
        this.departureLongitude = departureLongitude;
        this.arrivalLatitude = arrivalLatitude;
        this.arrivalLongitude = arrivalLongitude;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public void setDepartureLatitude(double departureLatitude) {
        this.departureLatitude = departureLatitude;
    }

    public void setDepartureLongitude(double departureLongitude) {
        this.departureLongitude = departureLongitude;
    }

    public void setArrivalLatitude(double arrivalLatitude) {
        this.arrivalLatitude = arrivalLatitude;
    }

    public void setArrivalLongitude(double arrivalLongitude) {
        this.arrivalLongitude = arrivalLongitude;
    }

    public String getTransportId() {
        return transportId;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public double getDepartureLatitude() {
        return departureLatitude;
    }

    public double getDepartureLongitude() {
        return departureLongitude;
    }

    public double getArrivalLatitude() {
        return arrivalLatitude;
    }

    public double getArrivalLongitude() {
        return arrivalLongitude;
    }
}
