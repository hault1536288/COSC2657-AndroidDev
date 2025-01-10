package com.example.easybooking.sampledata;

import com.example.easybooking.models.Transport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class TransportData {
    public static ArrayList<Transport> getSampleTransportData() {
        ArrayList<Transport> transportList = new ArrayList<>();

        transportList.add(new Transport(
                "T001", "Plane", "Airways X", new BigDecimal("199.99"),
                new Date(2025 - 1900, 0, 5, 10, 30), new Date(2025 - 1900, 0, 5, 14, 45),
                "New York", "London", 40.7128, -74.0060, 51.5074, -0.1278
        ));

        transportList.add(new Transport(
                "T002", "Train", "Express Y", new BigDecimal("49.99"),
                new Date(2025 - 1900, 0, 6, 8, 0), new Date(2025 - 1900, 0, 6, 12, 0),
                "Berlin", "Munich", 52.5200, 13.4050, 48.1351, 11.5820
        ));

        transportList.add(new Transport(
                "T003", "Bus", "CityBus Z", new BigDecimal("19.99"),
                new Date(2025 - 1900, 0, 7, 9, 15), new Date(2025 - 1900, 0, 7, 13, 45),
                "Paris", "Brussels", 48.8566, 2.3522, 50.8503, 4.3517
        ));

        transportList.add(new Transport(
                "T004", "Plane", "SkyHigh", new BigDecimal("299.99"),
                new Date(2025 - 1900, 0, 8, 16, 0), new Date(2025 - 1900, 0, 8, 19, 30),
                "Tokyo", "Seoul", 35.6895, 139.6917, 37.5665, 126.9780
        ));

        transportList.add(new Transport(
                "T005", "Train", "Regional Express", new BigDecimal("89.99"),
                new Date(2025 - 1900, 0, 9, 7, 30), new Date(2025 - 1900, 0, 9, 10, 0),
                "Rome", "Florence", 41.9028, 12.4964, 43.7696, 11.2558
        ));

        transportList.add(new Transport(
                "T006", "Bus", "InterCity A", new BigDecimal("29.99"),
                new Date(2025 - 1900, 0, 10, 6, 45), new Date(2025 - 1900, 0, 10, 10, 15),
                "Los Angeles", "San Francisco", 34.0522, -118.2437, 37.7749, -122.4194
        ));

        // Add more entries as needed...

        return transportList;
    }
}
