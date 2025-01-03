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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.easybooking.R;
import com.example.easybooking.models.Hotel;

import java.util.ArrayList;

public class HotelRecyclerViewAdapter extends RecyclerView.Adapter<HotelRecyclerViewAdapter.HotelViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Hotel> hotelArrayList;

    public HotelRecyclerViewAdapter(Context context, ArrayList<Hotel> hotelArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HotelRecyclerViewAdapter.HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hotel_item, parent, false);
        return new HotelViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelRecyclerViewAdapter.HotelViewHolder holder, int position) {
        Glide.with(context)
                .load(hotelArrayList.get(position).getHotelImageUrl())
                .placeholder(R.drawable.ic_hotel)
                .error(R.drawable.ic_hotel)
                .transform(new RoundedCorners(10))
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

        public HotelViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelRatingTextView = itemView.findViewById(R.id.hotelRatingTextView);
            hotelLocationTextView = itemView.findViewById(R.id.hotelLocationTextView);
            hotelPricePerNightTextView = itemView.findViewById(R.id.hotelPricePerNightTextView);
            hotelPricePerHourTextView = itemView.findViewById(R.id.hotelPricePerHourTextView);

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
