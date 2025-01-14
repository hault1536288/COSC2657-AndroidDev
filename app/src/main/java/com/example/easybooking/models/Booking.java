package com.example.easybooking.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    private String bookingId;
    private String userId;
    private String hotelId;
    private String hotelName;
    private String hotelLocation;
    private Date hotelFromDate;
    private Date hotelToDate;
    private String carId;
    private String carName;
    private Date carFromDate;
    private Date carToDate;
    private String transportId;
    private String transportDepartureLocation;
    private String transportArrivalLocation;
    private Date transportDepartureDate;
    private Date transportArrivalDate;
    private double totalAmount;
    private String status;

    public Booking() {
    }

    public Booking(String bookingId, String userId, String hotelId, String hotelName, String hotelLocation, Date hotelFromDate, Date hotelToDate, String carId, String carName, Date carFromDate, Date carToDate, String transportId, String transportDepartureLocation, String transportArrivalLocation, Date transportDepartureDate, Date transportArrivalDate, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
        this.hotelFromDate = hotelFromDate;
        this.hotelToDate = hotelToDate;
        this.carId = carId;
        this.carName = carName;
        this.carFromDate = carFromDate;
        this.carToDate = carToDate;
        this.transportId = transportId;
        this.transportDepartureLocation = transportDepartureLocation;
        this.transportArrivalLocation = transportArrivalLocation;
        this.transportDepartureDate = transportDepartureDate;
        this.transportArrivalDate = transportArrivalDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public Date getHotelFromDate() {
        return hotelFromDate;
    }

    public void setHotelFromDate(Date hotelFromDate) {
        this.hotelFromDate = hotelFromDate;
    }

    public Date getHotelToDate() {
        return hotelToDate;
    }

    public void setHotelToDate(Date hotelToDate) {
        this.hotelToDate = hotelToDate;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Date getCarFromDate() {
        return carFromDate;
    }

    public void setCarFromDate(Date carFromDate) {
        this.carFromDate = carFromDate;
    }

    public Date getCarToDate() {
        return carToDate;
    }

    public void setCarToDate(Date carToDate) {
        this.carToDate = carToDate;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getTransportDepartureLocation() {
        return transportDepartureLocation;
    }

    public void setTransportDepartureLocation(String transportDepartureLocation) {
        this.transportDepartureLocation = transportDepartureLocation;
    }

    public String getTransportArrivalLocation() {
        return transportArrivalLocation;
    }

    public void setTransportArrivalLocation(String transportArrivalLocation) {
        this.transportArrivalLocation = transportArrivalLocation;
    }

    public Date getTransportDepartureDate() {
        return transportDepartureDate;
    }

    public void setTransportDepartureDate(Date transportDepartureDate) {
        this.transportDepartureDate = transportDepartureDate;
    }

    public Date getTransportArrivalDate() {
        return transportArrivalDate;
    }

    public void setTransportArrivalDate(Date transportArrivalDate) {
        this.transportArrivalDate = transportArrivalDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (hotelFromDate != null && hotelToDate != null) {
            return dateFormat.format(hotelFromDate) + " → " + dateFormat.format(hotelToDate);
        }
        return "N/A";
    }

    public String getLocationRange() {
        if (transportDepartureLocation != null && transportArrivalLocation != null) {
            return transportDepartureLocation + " → " + transportArrivalLocation;
        }
        return "N/A";
    }
}
