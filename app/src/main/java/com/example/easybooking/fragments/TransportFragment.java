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
import android.widget.Toast;

import com.example.easybooking.R;
import com.example.easybooking.activities.TransportTicketDetailActivity;
import com.example.easybooking.adapters.RecyclerViewInterface;
import com.example.easybooking.adapters.TransportRecyclerViewAdapter;
import com.example.easybooking.models.Transport;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransportFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView transportsRecyclerView;
    private ArrayList<Transport> transportArrayList;
    private TransportRecyclerViewAdapter adapter;
    private FirebaseFirestore firestore;

    public TransportFragment() {
        // Required empty public constructor
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

        transportsRecyclerView = view.findViewById(R.id.transportsRecyclerView);
        transportArrayList = new ArrayList<>();
        adapter = new TransportRecyclerViewAdapter(getContext(), transportArrayList, this);

        transportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transportsRecyclerView.setAdapter(adapter);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        fetchTransportData();
    }

    private void fetchTransportData() {
        firestore.collection("transports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            transportArrayList.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Transport transport = document.toObject(Transport.class);
                                if (transport != null) {
                                    transport.setTransportId(document.getId()); // Set document ID as transport ID
                                    transportArrayList.add(transport);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("TransportFragment", "Error fetching data", task.getException());
                        Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TransportFragment", "Error fetching data", e);
                    Toast.makeText(getContext(), "Failed to load transports", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), TransportTicketDetailActivity.class);
        Transport selectedTransport = transportArrayList.get(position);

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
