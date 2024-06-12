package com.alya.aplikasilansia.ui.healthcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alya.aplikasilansia.R;

import java.util.List;

public class HealthCareAdapter extends RecyclerView.Adapter<HealthCareAdapter.ViewHolder> {
    private List<HealthCareService> healthCareServices;

    public HealthCareAdapter(List<HealthCareService> healthCareServices){
        this.healthCareServices = healthCareServices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_care, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthCareAdapter.ViewHolder holder, int position) {
        HealthCareService healthcareService = healthCareServices.get(position);
        holder.bind(healthcareService); // Call bind() method to bind data to views
    }

    @Override
    public int getItemCount() {
        return healthCareServices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView addressTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_healthcare_name);
            addressTextView = itemView.findViewById(R.id.tv_healthcare_address);
        }

        public void bind(HealthCareService healthcareService) {
            nameTextView.setText(healthcareService.getName());
            addressTextView.setText(healthcareService.getAddress());
        }
    }
}
