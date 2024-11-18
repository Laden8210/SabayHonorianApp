package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.Message;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.view.RideDetailActivity;
import com.example.sabayhonorianapp.view.ViewMessage;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private Context context;
    private List<Message> messages;

    private FirestoreRepositoryImpl<UserAccount> messageRepository;
    private GenericService<UserAccount> messageService;
    public InboxAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        messageRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        messageService = new GenericService<>(messageRepository);
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inbox, parent, false);

        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        Message message = messages.get(position);
        messageService.readItem(message.getReceiverUID(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                UserAccount userAccount = (UserAccount) result;
                holder.tvName.setText(userAccount.getFirstName() + " " + userAccount.getLastName());
                holder.cardView.setOnClickListener(e -> {
                    Intent intent = new Intent(context, ViewMessage.class);
                    intent.putExtra("receiverId", userAccount.getUserUID());
                    context.startActivity(intent);
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class InboxViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvName;
        public InboxViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textView);
            cardView = itemView.findViewById(R.id.cardViewMessage);
        }
    }
}
