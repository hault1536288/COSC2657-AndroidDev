package com.example.easybooking.utils;

import com.example.easybooking.models.Transport;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreUtils {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addTransport(Transport transport, FirestoreCallback callback) {
        Map<String, Object> transportData = new HashMap<>();
        transportData.put("transportId", transport.getTransportId());
        transportData.put("type", transport.getType());
        transportData.put("brand", transport.getBrand());
        transportData.put("price", transport.getPrice());
        transportData.put("departureDate", transport.getDepartureDate());
        transportData.put("arrivalDate", transport.getArrivalDate());
        transportData.put("departureLocation", transport.getDepartureLocation());
        transportData.put("arrivalLocation", transport.getArrivalLocation());
        transportData.put("departureLatitude", transport.getDepartureLatitude());
        transportData.put("departureLongitude", transport.getDepartureLongitude());
        transportData.put("arrivalLatitude", transport.getArrivalLatitude());
        transportData.put("arrivalLongitude", transport.getArrivalLongitude());

        // Use the transport ID as the document ID
        db.collection("transports")
                .document(transport.getTransportId())
                .set(transportData)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Transport added with ID: " + transport.getTransportId()))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public interface FirestoreCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
