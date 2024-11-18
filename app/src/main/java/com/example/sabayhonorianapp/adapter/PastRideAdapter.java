package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.model.BookRide;

import java.util.List;

public class PastRideAdapter extends RecyclerView.Adapter<PastRideAdapter.PastRideViewHolder> {


    private Context context;

    private List<BookRide> bookRides;

    public PastRideAdapter(Context context, List<BookRide> bookRides) {
        this.context = context;
        this.bookRides = bookRides;
    }

    @NonNull
    @Override
    public PastRideAdapter.PastRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new PastRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastRideAdapter.PastRideViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return bookRides.size();
    }

    public class PastRideViewHolder extends RecyclerView.ViewHolder {
        public PastRideViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
