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
import com.example.easybooking.adapters.TransportRecyclerViewAdapter;
import com.example.easybooking.models.Transport;
import com.example.easybooking.sampledata.TransportData;

import java.util.ArrayList;

public class TransportFragment extends Fragment {

    private RecyclerView transportsRecyclerView;

    public TransportFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get the recycler view from the layout
        transportsRecyclerView = view.findViewById(R.id.transportsRecyclerView);
        // get the transport data from the sample data
        ArrayList<Transport> transportArrayList = TransportData.getSampleTransportData();
        // set up adapter and layout manager for the recycler view
        TransportRecyclerViewAdapter adapter = new TransportRecyclerViewAdapter(getContext(), transportArrayList);
        transportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transportsRecyclerView.setAdapter(adapter);
    }
}