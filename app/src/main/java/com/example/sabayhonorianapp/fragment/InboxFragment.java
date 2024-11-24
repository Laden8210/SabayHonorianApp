package com.example.sabayhonorianapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.InboxAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.callback.FirestoreInboxCallback;
import com.example.sabayhonorianapp.model.Message;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxFragment extends Fragment {

    private RecyclerView rvInbox;


    private FirestoreRepositoryImpl<Message> messageRepository;
    private GenericService<Message> messageService;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        rvInbox = view.findViewById(R.id.rvInbox);

        rvInbox.setLayoutManager(new LinearLayoutManager(getContext()));

        messageRepository = new FirestoreRepositoryImpl<>("messages", Message.class);
        messageService = new GenericService<>(messageRepository);

        loadInbox();
        return view;
    }

    private void loadInbox() {
        String senderID = auth.getCurrentUser().getUid();

        messageRepository.readAllInbox(new FirestoreInboxCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {

                List<Message> messages = new ArrayList<>();

                for (Message message : result) {
                    if (message.getReceiverUID().equalsIgnoreCase(senderID)) {
                        messages.add(message);
                    }
                }

                rvInbox.setAdapter(new InboxAdapter(getContext(), messages));
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the error
            }
        });
    }


}