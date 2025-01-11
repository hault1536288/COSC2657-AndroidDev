package com.example.easybooking.fragments;

import android.content.Intent;
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
import com.example.easybooking.activities.TransportTicketDetailActivity;
import com.example.easybooking.adapters.RecyclerViewInterface;
import com.example.easybooking.adapters.TransportRecyclerViewAdapter;
import com.example.easybooking.models.Transport;
import com.example.easybooking.sampledata.TransportData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransportFragment extends Fragment implements RecyclerViewInterface {

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
        TransportRecyclerViewAdapter adapter = new TransportRecyclerViewAdapter(getContext(), transportArrayList, this);
        transportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transportsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), TransportTicketDetailActivity.class);
        Transport selectedTransport = TransportData.getSampleTransportData().get(position);

        // Create SimpleDateFormat objects for time and date
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Format departure and arrival times and dates
        String departureTime = timeFormat.format(selectedTransport.getDepartureDate());
        String departureDate = dateFormat.format(selectedTransport.getDepartureDate());
        String arrivalTime = timeFormat.format(selectedTransport.getArrivalDate());
        String arrivalDate = dateFormat.format(selectedTransport.getArrivalDate());

        intent.putExtra("TRANSPORT_ID", selectedTransport.getTransportId());
        intent.putExtra("TRANSPORT_TYPE", selectedTransport.getType());
        intent.putExtra("BRAND", selectedTransport.getBrand());
        intent.putExtra("DEPARTURE_LOCATION", selectedTransport.getDepartureLocation());
        intent.putExtra("DEPARTURE_TIME", departureTime);
        intent.putExtra("DEPARTURE_DATE", departureDate);
        intent.putExtra("ARRIVAL_LOCATION", selectedTransport.getArrivalLocation());
        intent.putExtra("ARRIVAL_TIME", arrivalTime);
        intent.putExtra("ARRIVAL_DATE", arrivalDate);
        intent.putExtra("PRICE", selectedTransport.getPrice());

        startActivity(intent);

    }
}