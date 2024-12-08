package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserAccount> userList;

    private Context context;

    private FirestoreRepositoryImpl<PostRide> repository;


    public UserAdapter(Context context, List<UserAccount> userList) {
        this.userList = userList;
        this.context = context;

        repository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserAccount user = userList.get(position);
        holder.userName.setText(user.getFirstName() + " " + user.getLastName());
//        holder.userCommission.setText("₱" + user.getCommission());
        Log.d("UserAdapter", "onBindViewHolder: " + user.getUserUID());
        repository.readAll("authorUID", user.getUserUID(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                List<PostRide> postRideList = (List<PostRide>) result;
                Log.d("UserAdapter", "onSuccess: " + postRideList.size());
                double commission = 0;
                for (PostRide postRide : postRideList) {

                    if (postRide.getStatus().equalsIgnoreCase("completed")){
                        commission += postRide.getPrice() * 0.1;
                    }

                }

                holder.userCommission.setText("₱" + commission);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userCommission;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_user_name);
            userCommission = itemView.findViewById(R.id.tv_user_commission);
        }
    }
}
