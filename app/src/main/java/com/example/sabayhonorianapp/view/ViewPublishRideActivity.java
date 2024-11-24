package com.example.sabayhonorianapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.JoinRideAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.callback.Listener;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Messenger;

import java.util.ArrayList;
import java.util.List;

public class ViewPublishRideActivity extends AppCompatActivity {

    private FirestoreRepositoryImpl<BookRide> bookRideRepository;
    private GenericService<BookRide> bookRideService;
    private Button btnStartRide;
    private PostRide ride;
    private RecyclerView rvPublishRide;

    private TextView tvRideDescription, tvLocationStart1, tvLocationStart2, tvPassengerLimit, tvTimeStart1, tvTimeStart2, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_publish_ride);

        rvPublishRide = findViewById(R.id.recyclerView);
        btnStartRide = findViewById(R.id.button2);

        bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
        bookRideService = new GenericService<>(bookRideRepository);

        ride = (PostRide) getIntent().getParcelableExtra("postRide");
        if (ride == null) {
            Log.e("ViewPublishRideActivity", "Failed to retrieve ride object. Intent extra 'ride' is null.");
        }

        btnStartRide.setOnClickListener(this::startRideAction);

        tvRideDescription = findViewById(R.id.tvRideDescription);
        tvLocationStart1 = findViewById(R.id.tvLocationStart1);
        tvLocationStart2 = findViewById(R.id.tvLocationStart2);
        tvPassengerLimit = findViewById(R.id.tvPassengerLimit);
        tvTimeStart1 = findViewById(R.id.tvTimeStart1);
        tvTimeStart2 = findViewById(R.id.tvTimeStart2);
        tvDate = findViewById(R.id.tvDate);


        if (getIntent().hasExtra("postRide")){
            PostRide postRide = getIntent().getParcelableExtra("postRide");
            tvRideDescription.setText(postRide.getDescription());
            tvLocationStart1.setText(postRide.getOrigin());
            tvLocationStart2.setText(postRide.getDestination());
            tvPassengerLimit.setText("Max, "+ postRide.getAvailableSeats() + "  passengers");




        }

        loadData();
    }

    private void startRideAction(View view) {
        if (ride == null) {
            Messenger.showAlertDialog(this, "Error", "Ride details are missing. Please try again.", "Ok").show();
            return;
        }

        Messenger.showAlertDialog(this, "Ride Now", "Are you sure you want to start the ride?", "Ok", "Cancel",
                (dialog, which) -> {
                    if (ride.getStatus() != null && ride.getStatus().equalsIgnoreCase("Completed")) {
                        Messenger.showAlertDialog(ViewPublishRideActivity.this, "Ride Completed", "This ride has already been completed", "Ok").show();
                        return;
                    }

                    Intent intent = new Intent(ViewPublishRideActivity.this, RideNavigationRide.class);
                    PostRide rideId = getIntent().getParcelableExtra("postRide");
                    intent.putExtra("postRide", rideId.getPostUID());
                    startActivity(intent);
                },
                (dialog, which) -> dialog.dismiss()
        ).show();
    }

    private void loadData() {
        PostRide rideId = getIntent().getParcelableExtra("postRide");
        if (rideId == null) {
            Log.e("ViewPublishRideActivity", "Ride ID is null.");
            return;
        }

        Log.d("ViewPublishRideActivity", "Loading data for rideId: " + rideId);

        bookRideRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                if (result instanceof List<?>) {
                    List<BookRide> bookRides = (List<BookRide>) result;
                    List<BookRide> filterRide = new ArrayList<>();
                    for (BookRide bookRide : bookRides) {
                        if (bookRide.getPostRideId().equals(rideId.getPostUID())) {
                            filterRide.add(bookRide);
                        }
                    }

                    rvPublishRide.setLayoutManager(new LinearLayoutManager(ViewPublishRideActivity.this));
                    rvPublishRide.setAdapter(new JoinRideAdapter(new Listener() {
                        @Override
                        public void listen() {
                            loadData();
                        }
                    }, ViewPublishRideActivity.this, filterRide));
                } else {
                    Log.e("ViewPublishRideActivity", "Unexpected data type received for BookRide list.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ViewPublishRideActivity", "Failed to fetch BookRide data.", e);
            }
        });
    }
}
