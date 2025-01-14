package com.example.easybooking.fragments;

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
import android.widget.Toast;

import com.example.easybooking.R;
import com.example.easybooking.adapters.CarRecyclerViewAdapter;
import com.example.easybooking.models.Car;
import com.example.easybooking.models.Hotel;
import com.example.easybooking.sampledata.CarData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CarFragment extends Fragment {
    private RecyclerView carsRecyclerView;
    private ArrayList<Car> carArrayList;
    private CarRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    public CarFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carsRecyclerView = view.findViewById(R.id.carsRecyclerView);
        carArrayList = new ArrayList<>();
        adapter = new CarRecyclerViewAdapter(getContext(), carArrayList);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carsRecyclerView.setAdapter(adapter);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchCarData();
    }

    private void fetchCarData() {
        firestore.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            carArrayList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Car car = document.toObject(Car.class);
                                if (car != null) {
                                    car.setCarId(document.getId()); // Set document ID as hotel ID
                                    carArrayList.add(car);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("CarFragment", "Error fetching data", task.getException());
                        Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CarFragment", "Error fetching data", e);
                    Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                });
    }

}