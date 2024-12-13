package com.example.sabayhonorianapp.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Loader;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RideDetailActivity extends AppCompatActivity {

    private TextView tvDate, tvTimeStart1, tvLocationStart1, tvTimeStart2, tvLocationEnd, tvAuthorName, tvPassengerLimit, tvRideDescription, tvContactAuthor;
    private Button requestRideButton;

    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private GenericService<PostRide> postRideService;

    private Loader loader;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);
        loader = new Loader();

        tvDate = findViewById(R.id.tvDate);
        tvTimeStart1 = findViewById(R.id.tvTimeStart1);
        tvLocationStart1 = findViewById(R.id.tvLocationStart1);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvPassengerLimit = findViewById(R.id.tvPassengerLimit);

        tvRideDescription = findViewById(R.id.tvRideDescription);
        requestRideButton = findViewById(R.id.button);
        tvContactAuthor = findViewById(R.id.tvContactAuthor);

        tvTimeStart2 = findViewById(R.id.tvTimeStart2);
        tvLocationEnd = findViewById(R.id.tvLocationStart2);

        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        postRideService = new GenericService<>(postRideRepository);

        if (getIntent().hasExtra("postRide")) {
            String postRideId = getIntent().getStringExtra("postRide");
            postRideService.readItem(postRideId, new FirestoreCallback() {
                @Override
                public void onSuccess(Object result) {
                    PostRide postRide = (PostRide) result;

                    // Format the date and time
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                    // Set formatted ride date
                    if (postRide.getRideDate() != null) {
                        String formattedDate = dateFormat.format(postRide.getRideDate());
                        tvDate.setText(formattedDate);
                    } else {
                        tvDate.setText("N/A");
                    }


                    // Set other ride details
                    tvLocationStart1.setText(postRide.getOrigin() != null ? postRide.getOrigin() : "N/A");
                    tvLocationEnd.setText(postRide.getDestination() != null ? postRide.getDestination() : "N/A");
                    tvAuthorName.setText(postRide.getAuthorName() != null ? postRide.getAuthorName() : "N/A");
                    tvPassengerLimit.setText(postRide.getAvailableSeats() > 0 ? String.valueOf("Max available set " + postRide.getAvailableSeats()) : "N/A");

                    tvRideDescription.setText(postRide.getDescription() != null ? postRide.getDescription() : "N/A");
                    tvContactAuthor.setText(postRide.getAuthorName() != null ? "Contact " +  postRide.getAuthorName() : "N/A");

                    tvContactAuthor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Messenger.showAlertDialog(RideDetailActivity.this, "Contact Author", "Contacting author...", "Ok", "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(RideDetailActivity.this, ViewMessage.class);
                                    intent.putExtra("receiverId", postRide.getAuthorUID());

                                    intent.putExtra("senderId", mAuth.getCurrentUser().getUid());
                                    startActivity(intent);
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    // Handle failure
                }
            });

            requestRideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loader.showLoader(RideDetailActivity.this);
                    requestRide();
                }
            });
        }
    }

    private void requestRide() {
        FirestoreRepositoryImpl<BookRide> bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
        GenericService<BookRide> bookRideService = new GenericService<>(bookRideRepository);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        BookRide bookRide = new BookRide();
        bookRide.setPostRideId(getIntent().getStringExtra("postRide"));
        bookRide.setUserId(auth.getCurrentUser().getUid());
        bookRide.setStatus("Pending");

        bookRideService.readItemByField("userId", auth.getCurrentUser().getUid(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                loader.dismissLoader();
                BookRide getbookRide = (BookRide) result;

                Log.d("BookRide", getbookRide.getPostRideId());
                Log.d("PostRide", getIntent().getStringExtra("postRide"));

                if (getbookRide.getPostRideId().equalsIgnoreCase(getIntent().getStringExtra("postRide"))) {
                    Messenger.showAlertDialog(RideDetailActivity.this, "Request Ride", "You have already requested a ride", "Ok").show();
                }else {
                    bookRideService.createItem(bookRide, new FirestoreCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            loader.dismissLoader();
                            Messenger.showAlertDialog(RideDetailActivity.this, "Request Ride", "Ride requested successfully", "Ok").show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            loader.dismissLoader();
                            Messenger.showAlertDialog(RideDetailActivity.this, "Request Ride", "Failed to request ride", "Ok").show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Exception e) {
                bookRideService.createItem(bookRide, new FirestoreCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        loader.dismissLoader();
                        Messenger.showAlertDialog(RideDetailActivity.this, "Request Ride", "Ride requested successfully", "Ok").show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        loader.dismissLoader();
                        Messenger.showAlertDialog(RideDetailActivity.this, "Request Ride", "Failed to request ride", "Ok").show();
                    }
                });

            }
        });




    }
}
