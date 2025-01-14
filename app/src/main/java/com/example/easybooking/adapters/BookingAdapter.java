package com.example.easybooking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easybooking.R;
import com.example.easybooking.fragments.BookingFragment;
import com.example.easybooking.models.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookingList;
    private final BookingFragment fragment;

    public BookingAdapter(List<Booking> bookingList, BookingFragment fragment) {
        this.bookingList = bookingList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set booking details
        holder.bookingIdTextView.setText(booking.getBookingId());
        holder.dateRangeTextView.setText(booking.getDateRange());
        holder.hotelNameTextView.setText(booking.getHotelName());
        holder.locationTextView.setText(booking.getLocation());
        holder.totalAmountTextView.setText("Total: " + booking.getTotalAmount());
        holder.statusTextView.setText(booking.getStatus());

        // Show or hide buttons based on booking status
        if (booking.isCurrentBooking()) {
            holder.buttonLayout.setVisibility(View.VISIBLE);

            // Handle "Go to Checkout" button click
            holder.checkoutButton.setOnClickListener(v -> {
                fragment.navigateToCheckoutFragment();
            });
        } else {
            holder.buttonLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdTextView, dateRangeTextView, hotelNameTextView, locationTextView, totalAmountTextView, statusTextView;
        Button cancelButton, checkoutButton;
        View buttonLayout;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingIdTextView = itemView.findViewById(R.id.bookingIdTextView);
            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            totalAmountTextView = itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            checkoutButton = itemView.findViewById(R.id.goToCheckoutButton);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
        }
    }
}
