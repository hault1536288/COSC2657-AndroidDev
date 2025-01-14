package com.example.easybooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.models.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.BookingViewHolder> {
    Context context;
    ArrayList<Booking> bookingArrayList;

    public BookingRecyclerViewAdapter(Context context, ArrayList<Booking> bookingArrayList) {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
    }

    @NonNull
    @Override
    public BookingRecyclerViewAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRecyclerViewAdapter.BookingViewHolder holder, int position) {
        // Assigns data to the views in each item in the RecyclerView
        // based on the position of the item in the list
        Booking booking = bookingArrayList.get(position);
        holder.dateRangeTextView.setText(booking.getDateRange());
        holder.bookingIdTextView.setText("Booking ID: " + booking.getBookingId());
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.locationTextView.setText(booking.getLocationRange());
        holder.carInfoTextView.setText(booking.getCarName());
        holder.totalAmountTextView.setText("$" + String.valueOf(booking.getTotalAmount()));
        holder.statusTextView.setText(booking.getStatus());
    }

    @Override
    public int getItemCount() {
        // Returns the number of items in the RecyclerView
        return bookingArrayList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout here (from the item_booking layout file)
        // kinda like the OnCreate method
        TextView dateRangeTextView, bookingIdTextView, hotelNameTextView, locationTextView, carInfoTextView, totalAmountTextView, statusTextView;
        Button cancelButton, checkoutButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            bookingIdTextView = itemView.findViewById(R.id.bookingIdTextView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            carInfoTextView = itemView.findViewById(R.id.carInfoTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            checkoutButton = itemView.findViewById(R.id.checkoutButton);
        }
    }
}
