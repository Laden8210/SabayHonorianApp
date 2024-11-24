package com.example.sabayhonorianapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.PastRideAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Loader;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;

    private FirestoreRepositoryImpl<BookRide> bookRideFirestoreRepository;
    private GenericService<BookRide> bookRideService;
    private FirebaseAuth mAuth;
    private Loader loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loader = new Loader();
        loader.showLoader(getActivity());
        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recyclerView_rides);

        bookRideFirestoreRepository = new FirestoreRepositoryImpl<>("bookRide",BookRide.class);
        bookRideService = new GenericService<>(bookRideFirestoreRepository);

        bookRideFirestoreRepository.readAll(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<BookRide> bookRides = (List<BookRide>) result;

                List<BookRide> filteredBookRides = new ArrayList<>();

                for (BookRide bookRide : bookRides) {
                    if (bookRide.getUserId().equals(mAuth.getCurrentUser().getUid())) {
                        filteredBookRides.add(bookRide);
                    }
                }

                PastRideAdapter pastRideAdapter = new PastRideAdapter(getContext(), filteredBookRides);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(pastRideAdapter);
                loader.dismissLoader();
            }

            @Override
            public void onFailure(Exception e) {
                loader.dismissLoader();
            }
        });


        return view;
    }
}