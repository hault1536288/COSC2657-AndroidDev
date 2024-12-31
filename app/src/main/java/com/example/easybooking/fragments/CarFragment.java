package com.example.easybooking.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easybooking.R;
import com.example.easybooking.adapters.CarRecyclerViewAdapter;
import com.example.easybooking.models.Car;
import com.example.easybooking.sampledata.CarData;

import java.util.ArrayList;

public class CarFragment extends Fragment {
    private RecyclerView carsRecyclerView;

    public CarFragment(){
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
        // get the recycler view from the layout
        carsRecyclerView = view.findViewById(R.id.carsRecyclerView);
        // get the car data from the sample data
        ArrayList<Car> carArrayList = CarData.getExampleCarList();
        // set up adapter and layout manager for the recycler view
        CarRecyclerViewAdapter adapter = new CarRecyclerViewAdapter(getContext(), carArrayList);
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carsRecyclerView.setAdapter(adapter);
    }
}