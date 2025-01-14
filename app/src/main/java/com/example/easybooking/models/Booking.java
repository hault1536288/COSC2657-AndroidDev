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
    private String transportId;
    private String transportType;
    private String transportBrand;
    private String transportDepartureLocation;
    private String transportArrivalLocation;
    private Date transportDepartureDate;
    private Date transportArrivalDate;
    private String carId;
    private String carName;
    private String carBrand;
    private String carModel;
    private Date carFromDate;
    private Date carToDate;
    private double hotelTotal;
    private double transportTotal;
    private double carTotal;
    private double totalAmount;
    private String status;
    private boolean isCurrent;

    public Booking() {
    }

    public Booking(String bookingId, String userId, String hotelId, String hotelName, String hotelLocation, Date hotelFromDate,
                   Date hotelToDate, String transportId, String transportType, String transportBrand, String transportDepartureLocation,
                   String transportArrivalLocation, Date transportDepartureDate, Date transportArrivalDate, String carId, String carName,
                   String carBrand, String carModel, Date carFromDate, Date carToDate, double hotelTotal, double transportTotal,
                   double carTotal, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
        this.hotelFromDate = hotelFromDate;
        this.hotelToDate = hotelToDate;
        this.transportId = transportId;
        this.transportType = transportType;
        this.transportBrand = transportBrand;
        this.transportDepartureLocation = transportDepartureLocation;
        this.transportArrivalLocation = transportArrivalLocation;
        this.transportDepartureDate = transportDepartureDate;
        this.transportArrivalDate = transportArrivalDate;
        this.carId = carId;
        this.carName = carName;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carFromDate = carFromDate;
        this.carToDate = carToDate;
        this.hotelTotal = hotelTotal;
        this.transportTotal = transportTotal;
        this.carTotal = carTotal;
        this.totalAmount = hotelTotal + transportTotal + carTotal;
        this.status = status;
        this.isCurrent = true;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
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

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getTransportBrand() {
        return transportBrand;
    }

    public void setTransportBrand(String transportBrand) {
        this.transportBrand = transportBrand;
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

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
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

    public double getHotelTotal() {
        return hotelTotal;
    }

    public void setHotelTotal(double hotelTotal) {
        this.hotelTotal = hotelTotal;
    }

    public double getTransportTotal() {
        return transportTotal;
    }

    public void setTransportTotal(double transportTotal) {
        this.transportTotal = transportTotal;
    }

    public double getCarTotal() {
        return carTotal;
    }

    public void setCarTotal(double carTotal) {
        this.carTotal = carTotal;
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

    public String getTransportDepartureTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        if (transportDepartureDate != null) {
            return timeFormat.format(transportDepartureDate);
        }
        return "N/A";
    }

    public String getTransportDepartureDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (transportDepartureDate != null) {
            return dateFormat.format(transportDepartureDate);
        }
        return "N/A";
    }

    public String getTransportArrivalTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        if (transportArrivalDate != null) {
            return timeFormat.format(transportArrivalDate);
        }
        return "N/A";
    }

    public String getTransportArrivalDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (transportArrivalDate != null) {
            return dateFormat.format(transportArrivalDate);
        }
        return "N/A";
    }

    public String getSpecificDateRange(String type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm:ss a");

        String dateRange = "N/A";

        // Check based on type if it is hotel, transport, or car, and return the corresponding date range
        if ("hotel".equalsIgnoreCase(type) && hotelFromDate != null && hotelToDate != null) {
            dateRange = dateFormat.format(hotelFromDate) + " to " + dateFormat.format(hotelToDate);
        } else if ("transport".equalsIgnoreCase(type) && transportDepartureDate != null && transportArrivalDate != null) {
            dateRange = dateFormat.format(transportDepartureDate) + " to " + dateFormat.format(transportArrivalDate);
        } else if ("car".equalsIgnoreCase(type) && carFromDate != null && carToDate != null) {
            dateRange = dateFormat.format(carFromDate) + " to " + dateFormat.format(carToDate);
        }

        return dateRange;
    }

    public String getLocationRange() {
        if (transportDepartureLocation != null && transportArrivalLocation != null) {
            return transportDepartureLocation + " → " + transportArrivalLocation;
        }
        return "N/A";
    }
}
