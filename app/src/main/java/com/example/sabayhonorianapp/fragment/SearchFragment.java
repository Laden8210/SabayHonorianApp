package com.example.sabayhonorianapp.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.PublishRideAdapter;
import com.example.sabayhonorianapp.adapter.RideAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Loader;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private RecyclerView rvRide;

    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private GenericService<PostRide> postRideService;
    private FirebaseAuth mAuth;
    Loader loader;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        rvRide = view.findViewById(R.id.rvRide);

        loader = new Loader();

        loader.showLoader(getContext());

        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        postRideService = new GenericService<>(postRideRepository);
        mAuth = FirebaseAuth.getInstance();
        rvRide.setLayoutManager(new LinearLayoutManager(getContext()));
        loadPublishRide();
        return view;
    }

    private void loadPublishRide() {
        postRideRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<PostRide> postRides = (List<PostRide>) result;

                List<PostRide> filterRide = new ArrayList<>();

                for (PostRide postRide : postRides) {
                    if (!postRide.getStatus().equalsIgnoreCase("completed")){
                        filterRide.add(postRide);
                    }
                }

                rvRide.setAdapter(new RideAdapter(getContext(), filterRide));
                loader.dismissLoader();
            }

            @Override
            public void onFailure(Exception e) {
                loader.dismissLoader();
            }
        });
    }



}
