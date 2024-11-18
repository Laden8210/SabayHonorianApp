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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AvailableRideAdapter extends RecyclerView.Adapter<AvailableRideAdapter.AvailableRideViewHolder> {

    private Context context;
    private List<PostRide> postRides;

    public AvailableRideAdapter(Context context, List<PostRide> postRides) {
        this.context = context;
        this.postRides = postRides;
    }

    @NonNull
    @Override
    public AvailableRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_available_ride, parent, false);
        return new AvailableRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableRideViewHolder holder, int position) {
        PostRide postRide = postRides.get(position);

        // Convert Firebase Timestamp to Date
        if (postRide.getRideTime() != null) {
            Date rideTimeDate = postRide.getRideTime().toDate();
            holder.tvTimeStart.setText(formatTime(rideTimeDate));
        } else {
            holder.tvTimeStart.setText("N/A"); // Handle null case
        }

        if (postRide.getRideEnd() != null) {
            Date rideEndDate = postRide.getRideEnd().toDate();
            holder.tvTimeEnd.setText(formatTime(rideEndDate));
        } else {
            holder.tvTimeEnd.setText("N/A"); // Handle null case
        }

        holder.tvLocationStart.setText(postRide.getOrigin());
        holder.tvLocationEnd.setText(postRide.getDestination());
        holder.tvAvailableSeat.setText(String.valueOf(postRide.getAvailableSeats()));
        holder.tvAuthorName.setText(postRide.getAuthorName());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RideDetailActivity.class);
            intent.putExtra("postRide", postRide.getPostUID());
            context.startActivity(intent);
        });
    }

    private String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        return sdf.format(date);
    }


    @Override
    public int getItemCount() {
        return postRides.size();
    }

    public class AvailableRideViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvTimeStart, tvTimeEnd, tvLocationStart, tvLocationEnd, tvAvailableSeat, tvAuthorName, tvDescription;
        public AvailableRideViewHolder(View itemView) {
            super(itemView);
            tvTimeStart = itemView.findViewById(R.id.tv_time_start);
            tvTimeEnd = itemView.findViewById(R.id.tv_time_end);
            tvLocationStart = itemView.findViewById(R.id.tv_location_start);
            tvLocationEnd = itemView.findViewById(R.id.tv_location_end);
            tvAvailableSeat = itemView.findViewById(R.id.tv_available_seats);
            tvAuthorName = itemView.findViewById(R.id.textView);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
