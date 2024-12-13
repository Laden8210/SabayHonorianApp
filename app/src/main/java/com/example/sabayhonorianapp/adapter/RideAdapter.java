package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.view.RideDetailActivity;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private Context context;
    private final List<PostRide> rideList;

    public RideAdapter(Context context, List<PostRide> rideList) {
        this.rideList = rideList;
        this.context = context;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_ride, parent, false);
        return new RideViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        // Bind data to views
        PostRide ride = rideList.get(position);


        String formattedRideDate = formatTimestamp(ride.getRideDate(), "MMMM dd, yyyy");

        holder.tvAuthorName.setText(ride.getAuthorName());
        holder.tvVehicleType.setText(ride.getVehicleType());
        holder.tvOrigin.setText("From: " + ride.getOrigin());
        holder.tvDestination.setText("To: " + ride.getDestination());
        holder.tvRideDate.setText("Date: " + formattedRideDate);

        holder.tvAvailableSeats.setText("Seats: " + ride.getAvailableSeats());
        holder.tvStatus.setText(ride.getStatus());
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RideDetailActivity.class);
            intent.putExtra("ride", ride);
            intent.putExtra("postRide", ride.getPostUID());
            context.startActivity(intent);
        });

        // Update status background color dynamically (optional)
        if (ride.getStatus().equalsIgnoreCase("PENDING")) {
            holder.tvStatus.setBackgroundResource(R.drawable.rounded_bg_status_orange);

        } else {

        }
    }

    private String formatTimestamp(Timestamp timestamp, String dateFormat) {
        if (timestamp == null) return "Invalid timestamp"; // Handle null timestamps gracefully
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdf.format(new Date(timestamp.toDate().getTime()));
    }

    private String formatTimestamp(Date date, String dateFormat) {
        if (date == null) return "Invalid date"; // Handle null dates gracefully
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    static class RideViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthorName, tvVehicleType, tvOrigin, tvDestination,
                tvRideDate, tvRideTime, tvAvailableSeats, tvStatus;
        CardView cardView;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvRideDate = itemView.findViewById(R.id.tvRideDate);
            tvRideTime = itemView.findViewById(R.id.tvRideTime);
            tvAvailableSeats = itemView.findViewById(R.id.tvAvailableSeats);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
