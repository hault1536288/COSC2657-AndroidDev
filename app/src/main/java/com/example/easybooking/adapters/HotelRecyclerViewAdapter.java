package com.example.easybooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easybooking.R;
import com.example.easybooking.models.Hotel;

import java.util.ArrayList;

public class HotelRecyclerViewAdapter extends RecyclerView.Adapter<HotelRecyclerViewAdapter.HotelViewHolder> {
    Context context;
    ArrayList<Hotel> hotelArrayList;

    public HotelRecyclerViewAdapter(Context context, ArrayList<Hotel> hotelArrayList) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
    }

    @NonNull
    @Override
    public HotelRecyclerViewAdapter.HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hotel_item, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelRecyclerViewAdapter.HotelViewHolder holder, int position) {
        Glide.with(context)
                .load(hotelArrayList.get(position).getHotelImageUrl())
                .placeholder(R.drawable.ic_hotel)
                .error(R.drawable.ic_hotel)
                .into(holder.hotelImageView);
        holder.hotelNameTextView.setText(hotelArrayList.get(position).getName());
        holder.hotelRatingTextView.setText(String.valueOf(hotelArrayList.get(position).getRating()));
        holder.hotelLocationTextView.setText(hotelArrayList.get(position).getLocation());
        holder.hotelPricePerNightTextView.setText("$" + hotelArrayList.get(position).getPricePerNight().toString() + "/night");
        holder.hotelPricePerHourTextView.setText("$" + hotelArrayList.get(position).getPricePerHour().toString() + "/hour");
    }

    @Override
    public int getItemCount() {
        return hotelArrayList.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImageView;
        TextView hotelNameTextView, hotelRatingTextView, hotelLocationTextView, hotelPricePerNightTextView, hotelPricePerHourTextView;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelRatingTextView = itemView.findViewById(R.id.hotelRatingTextView);
            hotelLocationTextView = itemView.findViewById(R.id.hotelLocationTextView);
            hotelPricePerNightTextView = itemView.findViewById(R.id.hotelPricePerNightTextView);
            hotelPricePerHourTextView = itemView.findViewById(R.id.hotelPricePerHourTextView);
        }
    }
}
