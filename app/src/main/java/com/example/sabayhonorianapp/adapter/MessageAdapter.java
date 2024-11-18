package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.model.Message;
import com.google.firebase.auth.FirebaseAuth;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Message> messages;
    private static final int MESSAGE_SEND = 1;
    private static final int MESSAGE_RECEIVED = 2;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_SEND) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_sender, parent, false);
            return new SenderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_receiver, parent, false);
            return new ReceiverHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder.getItemViewType() == MESSAGE_SEND) {
            ((SenderHolder) holder).bind(message);
        } else {
            ((ReceiverHolder) holder).bind(message);
        }


    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Sender", "Sender: " + (messages.get(position).getSenderUID() == mAuth.getCurrentUser().getUid()));
        Log.d("Sender", "Sender: " + mAuth.getCurrentUser().getUid());
        Log.d("Sender", "Sender: " + messages.get(position).getSenderUID());
        return messages.get(position).getSenderUID().equals(mAuth.getCurrentUser().getUid()) ?  MESSAGE_SEND : MESSAGE_RECEIVED ;
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {

        private final TextView tvMessage;


        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);

        }

        public void bind(Message message) {
            tvMessage.setText(message.getMessage() != null ? message.getMessage() : "");

        }
    }

    public class SenderHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;


        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);

        }

        public void bind(Message message) {
            tvMessage.setText(message.getMessage() != null ? message.getMessage() : "");

        }
    }
}
