package com.example.easybooking.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easybooking.R;

public class CheckoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Find Views
        TextView checkoutTotalAmount = view.findViewById(R.id.checkoutTotalAmount);
        Button backButton = view.findViewById(R.id.checkoutBackButton);
        Button confirmButton = view.findViewById(R.id.checkoutConfirmButton);

        // Set sample total amount
        checkoutTotalAmount.setText(getString(R.string.total_amount, "$450"));

        // Handle Back Button Click
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Handle Confirm Button Click
        confirmButton.setOnClickListener(v -> showPaymentSuccessDialog());

        return view;
    }

    // Method to show a popup dialog for successful payment
    private void showPaymentSuccessDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Payment Successful")
                .setMessage("Your payment has been processed successfully!")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Navigate back to BookingFragment after confirming payment
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .show();
    }
}
