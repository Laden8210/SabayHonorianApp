package com.example.sabayhonorianapp.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.MessageAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.Message;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class ViewMessage extends AppCompatActivity {

    private int employeeID;
    private RecyclerView rvMessage;
    private MessageAdapter messageAdapter;
    private ImageView ivProfile;

    private ImageButton btnSend;
    private TextInputLayout tilMessage;

    private Handler handler;
    private Runnable retrieveMessagesRunnable;

    private String receiverID;

    private FirestoreRepositoryImpl<Message> messageRepository;
    private GenericService<Message> messageService;

    private TextView tvName;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        rvMessage = findViewById(R.id.rv_message);
        rvMessage.setLayoutManager(new LinearLayoutManager(this));

        ivProfile = findViewById(R.id.imageView3);
        ivProfile.setOnClickListener(v -> {
            finish();
        });

        messageRepository = new FirestoreRepositoryImpl<>("messages", Message.class);
        messageService = new GenericService<>(messageRepository);

        btnSend = findViewById(R.id.imageButton);
        tilMessage = findViewById(R.id.textInputLayout2);

        tvName = findViewById(R.id.textView6);

        btnSend.setOnClickListener(this::sendMessage);
        handler = new Handler();

        if (getIntent().hasExtra("senderId")) {
            receiverID = getIntent().getStringExtra("senderId");

            Log.d("ViewMessage", "receiverID: " + receiverID);

            FirestoreRepositoryImpl<UserAccount> userAccountRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);

            userAccountRepository.readByField("userUID", receiverID, new FirestoreCallback() {
                @Override
                public void onSuccess(Object result) {
                    UserAccount userAccount = (UserAccount) result;
                    tvName.setText(userAccount.getFirstName() + " " + userAccount.getLastName());
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("ViewMessage", "Failed to retrieve user account", e);
                }
            });
        } else {
            Log.e("ViewMessage", "No receiverID passed to ViewMessage");
        }

        retrieveMessagesRunnable = new Runnable() {
            @Override
            public void run() {
                retrieveMessages();
                handler.postDelayed(this, 5000); // Repeat every second
            }
        };

        handler.post(retrieveMessagesRunnable);
    }

    private void sendMessage(View view) {
        String message = tilMessage.getEditText().getText().toString();

        Message newMessage = new Message();
        newMessage.setSenderUID(mAuth.getCurrentUser().getUid());

        newMessage.setReceiverUID(getIntent().getStringExtra("receiverId"));
        newMessage.setMessage(message);
        newMessage.setTimestamp(Timestamp.now());
        messageService.createItem(newMessage, new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.d("ViewMessage", "Message sent successfully");
                tilMessage.getEditText().setText("");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ViewMessage", "Failed to send message", e);
                Messenger.showAlertDialog(ViewMessage.this, "Error", "Failed to send message", "Ok").show();
            }
        });


    }

    private void retrieveMessages() {
        messageRepository.getItemsOrderedByTimestamp(new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<Message> messages = (List<Message>) result;
                messageAdapter = new MessageAdapter(ViewMessage.this, messages);
                rvMessage.setAdapter(messageAdapter);
                if (messageAdapter.getItemCount() > 0){
                    rvMessage.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the periodic message retrieval
        handler.removeCallbacks(retrieveMessagesRunnable);
    }
}
