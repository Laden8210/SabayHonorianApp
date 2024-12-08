package com.example.sabayhonorianapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.PaymentAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DisplayPaymentActivity extends AppCompatActivity {

    private RecyclerView rvPaymentDetails;
    private TextView tvTotalPayment, tvCommission, tvNetPayment;
    private MaterialButton btnClose;

    // Data
    private PaymentAdapter paymentAdapter;
    private List<BookRide> bookRides = new ArrayList<>();
    private PostRide postRide;

    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private FirestoreRepositoryImpl<BookRide> bookRideRepository;
    private FirestoreRepositoryImpl<UserAccount> userAccountRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_payment);

        initViews();

        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
        userAccountRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);

        tvCommission = findViewById(R.id.tvCommission);
        tvNetPayment = findViewById(R.id.tvNetPayment);


        String postRideId = getIntent().getStringExtra("postRideId");
        if (postRideId == null) {
            tvTotalPayment.setText("Error: Ride details not found.");
            return;
        }

        Log.d("DisplayPaymentActivity", "onCreate: postRideId: " + postRideId);
        postRideRepository.readById(postRideId, new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                postRide = (PostRide) result;
                if (postRide != null) {
                    fetchBookRides(postRideId);
                } else {
                    tvTotalPayment.setText("Error: Ride details not found.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                tvTotalPayment.setText("Error: Failed to fetch ride details.");
            }
        });

        btnClose.setOnClickListener(v -> {
            startActivity(new Intent(this, HeroActivity.class));
        });
    }

    private void initViews() {
        rvPaymentDetails = findViewById(R.id.rvPaymentDetails);
        tvTotalPayment = findViewById(R.id.tvTotalPayment);
        btnClose = findViewById(R.id.btnClose);
    }

    private void fetchBookRides(String postRideId) {
        bookRideRepository.readAll("postRideId", postRideId, new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                bookRides = (List<BookRide>) result;
                setupRecyclerView();
                calculateAndDisplayPayments();
            }

            @Override
            public void onFailure(Exception e) {
                tvTotalPayment.setText("Error: Failed to fetch bookings.");
            }
        });
    }

    private void setupRecyclerView() {
        paymentAdapter = new PaymentAdapter(new ArrayList<>());
        rvPaymentDetails.setLayoutManager(new LinearLayoutManager(this));
        rvPaymentDetails.setAdapter(paymentAdapter);
    }

    private void calculateAndDisplayPayments() {
        if (postRide == null) {
            tvTotalPayment.setText("Error: Ride details not available.");
            return;
        }

        if (bookRides.isEmpty()) {
            tvTotalPayment.setText("No customers found for this ride.");
            return;
        }

        double totalPrice = postRide.getPrice();
        int totalCustomers = bookRides.size();
        double pricePerCustomer = totalPrice / totalCustomers;


        tvTotalPayment.setText(String.format("Total Payment: ₱%.2f", totalPrice));

        double commission = totalPrice * 0.1;
        double netPayment = totalPrice - commission;

        tvCommission.setText(String.format("Commission (10%): ₱%.2f", commission));
        tvNetPayment.setText(String.format("Net Payment: ₱%.2f", netPayment));

        List<String> customerPayments = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(bookRides.size());

        for (BookRide bookRide : bookRides) {
            userAccountRepository.readByField("userUID", bookRide.getUserId(), new FirestoreCallback() {
                @Override
                public void onSuccess(Object result) {
                    UserAccount userAccount = (UserAccount) result;
                    String customerName = "Customer: " + userAccount.getFirstName() + " " + userAccount.getLastName();
                    String payment = String.format("₱%.2f", pricePerCustomer);
                    customerPayments.add(customerName + " - " + payment);
                    latch.countDown();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("DisplayPaymentActivity", "Failed to fetch user details", e);
                    latch.countDown();
                }
            });
        }

        new Thread(() -> {
            try {
                latch.await();
                runOnUiThread(() -> paymentAdapter.updatePayments(customerPayments));
            } catch (InterruptedException e) {
                Log.e("DisplayPaymentActivity", "Error waiting for user data", e);
            }
        }).start();
    }
}
