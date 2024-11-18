package com.example.sabayhonorianapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sabayhonorianapp.MapActivity;
import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.PublishRideAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.fragment.bottomsheet.PublishRideBottomSheet;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Loader;
import com.example.sabayhonorianapp.view.CreateRideActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class PublishRideFragment extends Fragment {

    private ExtendedFloatingActionButton fabPublishRide;

    private PublishRideBottomSheet publishRideFragment;

    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private GenericService<PostRide> postRideService;

    private RecyclerView rvPublishRide;
    private FirebaseAuth mAuth;

    private Loader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_publish_ride, container, false);
        fabPublishRide = view.findViewById(R.id.fabAddRide);
        fabPublishRide.setOnClickListener(this::publishRideAction);
        publishRideFragment = new PublishRideBottomSheet();

        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        postRideService = new GenericService<>(postRideRepository);
        mAuth = FirebaseAuth.getInstance();
        rvPublishRide = view.findViewById(R.id.rvPublishRide);
        rvPublishRide.setLayoutManager(new LinearLayoutManager(getContext()));

        loader = new Loader();
        loader.showLoader(getContext());

        loadPublishRide();
        return view;

    }

    private void loadPublishRide() {
        postRideRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<PostRide> postRides = (List<PostRide>) result;
                List<PostRide> postRideList = new ArrayList<>();
                for (PostRide postRide : postRides) {
                    if (postRide.getAuthorUID().equals(mAuth.getCurrentUser().getUid())){
                        postRideList.add(postRide);
                    }
                }
                loader.dismissLoader();
                rvPublishRide.setAdapter(new PublishRideAdapter(getContext(), postRideList));
            }

            @Override
            public void onFailure(Exception e) {
                loader.dismissLoader();
            }
        });
    }

    private void publishRideAction(View view) {
//        publishRideFragment.show(getChildFragmentManager(), publishRideFragment.getTag());

        startActivity(new Intent(getContext(), MapActivity.class));
    }
}