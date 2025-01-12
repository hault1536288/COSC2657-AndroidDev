package com.example.easybooking.models;

public class Booking {

    private final String bookingId;
    private final String dateRange;
    private final String hotelName;
    private final String location;
    private final String carInfo; // New field for Car Information
    private final String totalAmount;
    private final String status;
    private final boolean isCurrentBooking; // New field to determine if it's a current booking

    // Constructor
    public Booking(String bookingId, String dateRange, String hotelName, String location, String carInfo, String totalAmount, String status, boolean isCurrentBooking) {
        this.bookingId = bookingId;
        this.dateRange = dateRange;
        this.hotelName = hotelName;
        this.location = location;
        this.carInfo = carInfo;
        this.totalAmount = totalAmount;
        this.status = status;
        this.isCurrentBooking = isCurrentBooking;
    }

    // Getters
    public String getBookingId() {
        return bookingId;
    }

    public String getDateRange() {
        return dateRange;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getLocation() {
        return location;
    }

    public String getCarInfo() {
        return carInfo != null ? carInfo : "N/A"; // Return "N/A" if car info is null
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCurrentBooking() {
        return isCurrentBooking;
    }

    // Optional: Setters if needed
}
