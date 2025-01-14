package com.example.easybooking.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easybooking.R;
import com.example.easybooking.activities.HotelDetailActivity;
import com.example.easybooking.adapters.HotelRecyclerViewAdapter;
import com.example.easybooking.adapters.RecyclerViewInterface;
import com.example.easybooking.models.Hotel;
import com.example.easybooking.models.Transport;
import com.example.easybooking.sampledata.HotelData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HotelFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView hotelsRecyclerView;
    private ArrayList<Hotel> hotelArrayList;
    private HotelRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    public HotelFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText locationEditText = view.findViewById(R.id.locationEditText);
        EditText dateEditText = view.findViewById(R.id.dateEditText);
        EditText typeEditText = view.findViewById(R.id.typeEditText);

        locationEditText.clearFocus();
        dateEditText.clearFocus();
        typeEditText.clearFocus();

        view.findViewById(R.id.hotelsRecyclerView).requestFocus();

        hotelsRecyclerView = view.findViewById(R.id.hotelsRecyclerView);
        hotelArrayList = new ArrayList<>();
        adapter = new HotelRecyclerViewAdapter(getContext(), hotelArrayList, this);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelsRecyclerView.setAdapter(adapter);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchHotelData();
    }

    private void fetchHotelData() {
        firestore.collection("hotels")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            hotelArrayList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Hotel hotel = document.toObject(Hotel.class);
                                if (hotel != null) {
                                    hotel.setHotelId(document.getId()); // Set document ID as hotel ID
                                    hotelArrayList.add(hotel);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("HotelFragment", "Error fetching data", task.getException());
                        Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("HotelFragment", "Error fetching data", e);
                    Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), HotelDetailActivity.class);
        // Get the selected hotel
        Hotel selectedHotel = HotelData.getExampleHotelList().get(position);
        // Pass the selected hotel to the HotelDetailActivity
        intent.putExtra("HOTEL_IMAGE_URL", selectedHotel.getImageUrl());
        intent.putExtra("HOTEL_NAME", selectedHotel.getHotelName());
        intent.putExtra("HOTEL_DESCRIPTION", selectedHotel.getDescription());
        intent.putExtra("HOTEL_LOCATION", selectedHotel.getLocation());
        intent.putExtra("HOTEL_LAT", selectedHotel.getLatitude());
        intent.putExtra("HOTEL_LNG", selectedHotel.getLongitude());

        startActivity(intent);
    }
}