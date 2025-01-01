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
import com.example.easybooking.adapters.HotelRecyclerViewAdapter;
import com.example.easybooking.models.Hotel;
import com.example.easybooking.sampledata.HotelData;

import java.util.ArrayList;

public class HotelFragment extends Fragment {

    private RecyclerView hotelsRecyclerView;

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
        // get the recycler view from the layout
        hotelsRecyclerView = view.findViewById(R.id.hotelsRecyclerView);
        // get the hotel data from the sample data
        ArrayList<Hotel> hotelArrayList = HotelData.getExampleHotelList();
        // set up adapter and layout manager for the recycler view
        HotelRecyclerViewAdapter adapter = new HotelRecyclerViewAdapter(getContext(), hotelArrayList);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelsRecyclerView.setAdapter(adapter);
    }
}