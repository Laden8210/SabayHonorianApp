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
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.view.CurrentBookingRideActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;


public class PastRideAdapter extends RecyclerView.Adapter<PastRideAdapter.PastRideViewHolder> {

    private Context context;
    private List<BookRide> bookRides;

    private FirestoreRepositoryImpl<PostRide> repository;
    public PastRideAdapter(Context context, List<BookRide> bookRides) {
        this.context = context;
        this.bookRides = bookRides;
        repository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
    }

    @NonNull
    @Override
    public PastRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new PastRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastRideViewHolder holder, int position) {
        BookRide bookRide = bookRides.get(position);

        holder.tvDriverName.setText(bookRide.getRideId());


        repository.readById(bookRide.getPostRideId(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                PostRide postRide = (PostRide) result;
                holder.tvDriverName.setText(postRide.getAuthorName());
                holder.tvPrice.setText("₱" + postRide.getPrice());
                holder.tvRoute.setText(postRide.getOrigin() + " ➔ " + postRide.getDestination());



                holder.tvSeatsAvailable.setText(postRide.getAvailableSeats() + " seats available");
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure case
                holder.tvTime.setText("Error loading data");
            }
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CurrentBookingRideActivity.class);
            intent.putExtra("bookRide", bookRide);
            context.startActivity(intent);

        });

        holder.tvStatus.setText(bookRide.getStatus());
        holder.tvStatus.setTextColor(context.getResources().getColor(
                bookRide.getStatus().equalsIgnoreCase("completed") ?
                        android.R.color.holo_green_dark : android.R.color.holo_red_dark));
    }

    @Override
    public int getItemCount() {
        return bookRides.size();
    }

    public static class PastRideViewHolder extends RecyclerView.ViewHolder {
        TextView tvDriverName, tvPrice, tvRoute, tvTime, tvSeatsAvailable, tvStatus;
        CardView cardView;
        public PastRideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDriverName = itemView.findViewById(R.id.tv_driver_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvRoute = itemView.findViewById(R.id.tv_route);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvSeatsAvailable = itemView.findViewById(R.id.tv_seats_available);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
