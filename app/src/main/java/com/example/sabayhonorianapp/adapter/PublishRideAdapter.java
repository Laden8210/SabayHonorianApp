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
import com.example.sabayhonorianapp.view.ViewPublishRideActivity;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PublishRideAdapter extends RecyclerView.Adapter<PublishRideAdapter.MyViewHolder> {

    private Context context;
    private List<PostRide> postRideList;

    public PublishRideAdapter(Context context, List<PostRide> postRideList) {
        this.context = context;
        this.postRideList = postRideList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_publish_ride, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostRide postRide = postRideList.get(position);

        holder.tvOrigin.setText(postRide.getOrigin());
        holder.tvDestination.setText(postRide.getDestination());

        // Format the ride date
        if (postRide.getRideDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(postRide.getRideDate());
            holder.tvRideDate.setText(formattedDate);
        } else {
            holder.tvRideDate.setText("N/A");
        }

        // Format the ride time

        holder.tvStatus.setText(postRide.getStatus());
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewPublishRideActivity.class);
                intent.putExtra("postRide", postRide);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return postRideList != null ? postRideList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrigin;
        TextView tvDestination;
        TextView tvRideDate;
        TextView tvRideTime;
        TextView tvStatus;

        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrigin = itemView.findViewById(R.id.tvOrigin);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvRideDate = itemView.findViewById(R.id.tvRideDate);
            tvRideTime = itemView.findViewById(R.id.tvRideTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cardView = itemView.findViewById(R.id.cardPublishRide);
        }
    }
}
