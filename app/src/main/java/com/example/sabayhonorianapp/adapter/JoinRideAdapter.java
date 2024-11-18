package com.example.sabayhonorianapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.callback.Listener;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class JoinRideAdapter extends RecyclerView.Adapter<JoinRideAdapter.MyViewHolder> {

    private Listener listener;
    private Context context;
    private List<BookRide> joinRideList;
    private FirestoreRepositoryImpl<UserAccount> userRepository;
    private GenericService<UserAccount> userService;
    public JoinRideAdapter(Listener listener,Context context, List<BookRide> joinRideList) {
        this.listener = listener;
        this.context = context;
        this.joinRideList = joinRideList;
        userRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        userService = new GenericService<>(userRepository);

    }



    @NonNull
    @Override
    public JoinRideAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_join_ride, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinRideAdapter.MyViewHolder holder, int position) {
        BookRide bookRide = joinRideList.get(position);

        userService.readItemByField("userUID", bookRide.getUserId(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                UserAccount userAccount = (UserAccount) result;
                holder.tvName.setText(userAccount.getFirstName() + " " + userAccount.getLastName());

                holder.tvStatus.setText(bookRide.getStatus());


                holder.cardView.setOnClickListener(e ->{
                    Messenger.showAlertDialog(context, "Accept Student", "Do you want to accept this student?", "Yes", "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirestoreRepositoryImpl<BookRide> bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
                                    GenericService<BookRide> bookRideService = new GenericService<>(bookRideRepository);
                                    bookRide.setStatus("Accepted");
                                    bookRideRepository.update(bookRide.getRideId(), bookRide, new FirestoreCallback() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            Messenger.showAlertDialog(context, "Success", "Student has been accepted", "Ok").show();
                                            listener.listen();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Messenger.showAlertDialog(context, "Error", e.getMessage(), "Ok").show();
                                        }
                                    });
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirestoreRepositoryImpl<BookRide> bookRideRepository = new FirestoreRepositoryImpl<>("bookRide", BookRide.class);
                                    GenericService<BookRide> bookRideService = new GenericService<>(bookRideRepository);
                                    bookRide.setStatus("Declined");
                                    bookRideRepository.update(bookRide.getRideId(), bookRide, new FirestoreCallback() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            Messenger.showAlertDialog(context, "Success", "Student has been declined", "Ok").show();
                                            listener.listen();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            Messenger.showAlertDialog(context, "Error", e.getMessage(), "Ok").show();
                                        }
                                    });
                                }
                            }).show();
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return joinRideList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        MaterialCardView cardView;
        TextView tvStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            cardView = itemView.findViewById(R.id.cardView);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }
}
