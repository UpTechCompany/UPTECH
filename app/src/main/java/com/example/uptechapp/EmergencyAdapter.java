package com.example.uptechapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<Emergency> emergenciesList;
    static final String TAG = "AdapterEmergency";

    public EmergencyAdapter(List<Emergency> emergenciesList) {
        this.emergenciesList = emergenciesList;
    }

    @NonNull
    @Override
    public EmergencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency, parent, false);
        return new EmergencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyAdapter.ViewHolder holder, int position) {

        Emergency emergency = emergenciesList.get(position);

        Log.i(TAG, "Emergency - " + emergency.getTitle());

        holder.setData(
                emergency.getTitle(),
                emergency.getDescription(),
                emergency.getTime(),
                emergency.getPhotoUrl()
        );
    }

    @Override
    public int getItemCount() {
        return emergenciesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView emergencyTitle, emergencyDescription, emergencyTime;
        private ImageView emergencyPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emergencyTitle = itemView.findViewById(R.id.textLabel);
            emergencyDescription = itemView.findViewById(R.id.textDescription);
            emergencyTime = itemView.findViewById(R.id.textDate);
            emergencyPhoto = itemView.findViewById(R.id.imageView);

        }

        private void setData(String title, String description, String time, String photo) {

        try {
            emergencyTitle.setText(title);
            emergencyDescription.setText(description);
            emergencyTime.setText(time.toString());
            emergencyPhoto.setImageResource(R.drawable.ic_google_logo);

            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }
}
