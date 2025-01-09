package com.example.easybooking.models;

import java.math.BigDecimal;
import java.util.Date;

public class Transport {
    private String transportId;
    private String type;
    private String brand;
    private BigDecimal price;
    private Date departureDate;
    private Date arrivalDate;
    private String departureLocation;
    private String arrivalLocation;
    private double departureLatitude;
    private double departureLongitude;
    private double arrivalLatitude;
    private double arrivalLongitude;
    public Transport() {}
    public Transport(String transportId, String type, String brand, BigDecimal price, Date departureDate,
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

    public String getTransportId() {
        return transportId;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrice() {
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
