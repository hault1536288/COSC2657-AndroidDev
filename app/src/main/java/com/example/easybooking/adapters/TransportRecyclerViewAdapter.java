package com.example.easybooking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.models.Transport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransportRecyclerViewAdapter extends RecyclerView.Adapter<TransportRecyclerViewAdapter.TransportViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Transport> transportArrayList;

    public TransportRecyclerViewAdapter(Context context, ArrayList<Transport> transportArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.transportArrayList = transportArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TransportRecyclerViewAdapter.TransportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transport_item, parent, false);
        return new TransportRecyclerViewAdapter.TransportViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportRecyclerViewAdapter.TransportViewHolder holder, int position) {
        // Assigns data to the views in each item in the RecyclerView
        // based on the position of the item in the list
        Transport transport = transportArrayList.get(position);

        // Set the icon based on the type
        switch (transport.getType().toLowerCase()) {
            case "plane":
                holder.typeIconImageView.setImageResource(R.drawable.ic_plane);
                break;
            case "train":
                holder.typeIconImageView.setImageResource(R.drawable.ic_train);
                break;
            default:
                holder.typeIconImageView.setImageResource(R.drawable.ic_transport);
                break;
        }
        holder.typeTextView.setText(transport.getType());

        // Define the time and date formats
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Format the departure and arrival dates
        String departureTime = timeFormat.format(transport.getDepartureDate());
        String departureDate = dateFormat.format(transport.getDepartureDate());
        String arrivalTime = timeFormat.format(transport.getArrivalDate());
        String arrivalDate = dateFormat.format(transport.getArrivalDate());

        holder.departureLocationTextView.setText(transport.getDepartureLocation());
        holder.departureTimeTextView.setText(departureTime);
        holder.departureDateTextView.setText(departureDate);

        holder.arrivalLocationTextView.setText(transport.getArrivalLocation());
        holder.arrivalTimeTextView.setText(arrivalTime);
        holder.arrivalDateTextView.setText(arrivalDate);

        // Calculate the travel time in milliseconds
        long departureTimeMillis = transport.getDepartureDate().getTime();
        long arrivalTimeMillis = transport.getArrivalDate().getTime();
        long travelTimeMillis = arrivalTimeMillis - departureTimeMillis;

        // Convert milliseconds to hours and minutes
        long hours = travelTimeMillis / (1000 * 60 * 60);
        long minutes = (travelTimeMillis % (1000 * 60 * 60)) / (1000 * 60);

        // Format the travel time as "hh:mm"
        @SuppressLint("DefaultLocale") String travelTime = String.format("%02dh%02dm", hours, minutes);
        holder.travelTimeTextView.setText(travelTime);

        holder.brandTextView.setText(transport.getBrand());
        holder.priceTextView.setText("$" + transport.getPrice());

    }

    @Override
    public int getItemCount() {
        return transportArrayList.size();
    }

    public static class TransportViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout here (from the transport_item layout file)
        // kinda like the OnCreate method
        ImageView typeIconImageView;
        TextView typeTextView, departureLocationTextView, departureTimeTextView, departureDateTextView;
        TextView arrivalLocationTextView, arrivalTimeTextView, arrivalDateTextView;
        TextView travelTimeTextView, brandTextView, priceTextView;

        public TransportViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            typeIconImageView = itemView.findViewById(R.id.typeIconImageView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            departureLocationTextView = itemView.findViewById(R.id.departureLocationTextView);
            departureTimeTextView = itemView.findViewById(R.id.departureTimeTextView);
            departureDateTextView = itemView.findViewById(R.id.departureDateTextView);
            arrivalLocationTextView = itemView.findViewById(R.id.arrivalLocationTextView);
            arrivalTimeTextView = itemView.findViewById(R.id.arrivalTimeTextView);
            arrivalDateTextView = itemView.findViewById(R.id.arrivalDateTextView);
            travelTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
            brandTextView = itemView.findViewById(R.id.brandTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                // This method is called when an item in the RecyclerView is clicked
                @Override
                public void onClick(View view) {
                    // Get the position of the item in the RecyclerView
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            // Call the onItemClick method of the RecyclerViewInterface
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
