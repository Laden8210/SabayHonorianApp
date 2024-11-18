package com.example.sabayhonorianapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_publish_ride);
        rvPublishRide = findViewById(R.id.recyclerView);

        bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
        bookRideService = new GenericService<>(bookRideRepository);


        ride = (PostRide) getIntent().getSerializableExtra("ride");



        btnStartRide = findViewById(R.id.button2);
        btnStartRide.setOnClickListener(this::startRideAction);
        loadData();


    }

    private void startRideAction(View view) {

        Messenger.showAlertDialog(this, "Ride Now", "Are you sure you want to start the ride?", "Ok", "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ViewPublishRideActivity.this, RideNavigationRide.class);


                        String uid = getIntent().getStringExtra("postRide");

                        intent.putExtra("postRide", uid);
                        startActivity(intent);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    private void loadData() {
        String rideId = getIntent().getStringExtra("postRide");
        Log.d("RideId", rideId);
        bookRideRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<BookRide> bookRides = (List<BookRide>) result;
                List<BookRide> filterRide = new ArrayList<>();
                for (BookRide bookRide : bookRides) {
                    if (bookRide.getPostRideId().equals(rideId)) {
                        filterRide.add(bookRide);
                    }
                }

                rvPublishRide.setLayoutManager(new LinearLayoutManager(ViewPublishRideActivity.this));
                rvPublishRide.setAdapter(new JoinRideAdapter(new Listener() {
                    @Override
                    public void listen() {
                        loadData();
                    }
                },ViewPublishRideActivity.this, filterRide));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}