package com.example.easybooking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easybooking.R;
import com.example.easybooking.activities.CarDetailActivity;
import com.example.easybooking.models.Car;

import java.util.ArrayList;

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.CarViewHolder> {
    Context context;
    ArrayList<Car> carArrayList;

    public CarRecyclerViewAdapter(Context context, ArrayList<Car> carArrayList) {
        this.context = context;
        this.carArrayList = carArrayList;
    }

    @NonNull
    @Override
    public CarRecyclerViewAdapter.CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where you inflate the layout for each item in the RecyclerView
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarRecyclerViewAdapter.CarViewHolder holder, int position) {
        // Assigns data to the views in each item in the RecyclerView
        // based on the position of the item in the list
        Glide.with(context)
                .load(carArrayList.get(position).getCarImageUrl())
                .placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car)
                .into(holder.carImageView);
        holder.carNameTextView.setText(carArrayList.get(position).getName());
        holder.carBrandTextView.setText("Brand: " + carArrayList.get(position).getBrand());
        holder.pricePerDayTextView.setText("$" + carArrayList.get(position).getPricePerDay() + "/day");
        holder.pricePerHourTextView.setText("$" + carArrayList.get(position).getPricePerHour() + "/hour");

        // Set up button click listener
        holder.viewDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CarDetailActivity.class);

            // Pass data to the CarDetailActivity
            intent.putExtra("CAR_ID", carArrayList.get(position).getCarId());
            intent.putExtra("CAR_IMAGE_URL", carArrayList.get(position).getCarImageUrl());
            intent.putExtra("CAR_NAME", carArrayList.get(position).getName());
            intent.putExtra("CAR_BRAND", carArrayList.get(position).getBrand());
            intent.putExtra("CAR_MODEL", carArrayList.get(position).getModel());
            intent.putExtra("CAR_DESCRIPTION", carArrayList.get(position).getDescription());
            intent.putExtra("CAR_PRICE_PER_DAY", carArrayList.get(position).getPricePerDay());
            intent.putExtra("CAR_PRICE_PER_HOUR", carArrayList.get(position).getPricePerHour());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Returns the number of items in the RecyclerView
        return carArrayList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        // Define views in the item layout here (from the car_item layout file)
        // kinda like the OnCreate method
        ImageView carImageView;
        TextView carNameTextView, carBrandTextView, pricePerDayTextView, pricePerHourTextView;
        Button viewDetailsButton;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            carImageView = itemView.findViewById(R.id.carImageView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            carBrandTextView = itemView.findViewById(R.id.carBrandTextView);
            pricePerDayTextView = itemView.findViewById(R.id.pricePerDayTextView);
            pricePerHourTextView = itemView.findViewById(R.id.pricePerHourTextView);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}
