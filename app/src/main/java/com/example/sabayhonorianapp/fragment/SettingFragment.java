package com.example.sabayhonorianapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.util.Messenger;
import com.example.sabayhonorianapp.view.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

   private Button btnLogout;
   FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btnLogout = view.findViewById(R.id.button3);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messenger.showAlertDialog(getContext(), " Logout", "Are you sure you want to logout?", "Yes", "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
                ).show();
            }
        });
        return view;
    }
}