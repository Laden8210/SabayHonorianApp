package com.example.sabayhonorianapp.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.UserAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirestoreRepositoryImpl<UserAccount> repository;

    private RecyclerView rvUser;

    private TextView tvTotalCommission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        rvUser = findViewById(R.id.rv_registered_users);

        repository = new FirestoreRepositoryImpl<>("user", UserAccount.class);

        tvTotalCommission = findViewById(R.id.tv_total_commission);

        repository.readAllUser(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<UserAccount> userList = (List<UserAccount>) result;

                UserAdapter userAdapter = new UserAdapter(AdminActivity.this, userList);
                rvUser.setAdapter(userAdapter);
                rvUser.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        FirestoreRepositoryImpl<PostRide> postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);

        postRideRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<PostRide> postRideList = (List<PostRide>) result;
                double totalCommission = 0;
                for (PostRide postRide : postRideList) {
                    if (postRide.getStatus().equalsIgnoreCase("completed")) {
                        totalCommission += postRide.getPrice() * 0.1;
                    }
                }
                tvTotalCommission.setText("₱" + totalCommission);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}