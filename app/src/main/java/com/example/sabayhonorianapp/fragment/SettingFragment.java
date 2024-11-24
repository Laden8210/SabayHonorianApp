package com.example.sabayhonorianapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.util.Messenger;
import com.example.sabayhonorianapp.view.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingFragment extends Fragment {

    private Button btnLogout, btnEditProfile;
    private TextView tvFullName, tvGender;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirestoreRepositoryImpl<UserAccount> db = new FirestoreRepositoryImpl<>("user", UserAccount.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Initialize Views
        tvFullName = view.findViewById(R.id.fullname);
        tvGender = view.findViewById(R.id.gender);
        btnLogout = view.findViewById(R.id.logoutButton);
        btnEditProfile = view.findViewById(R.id.editButton);

        // Load user information
        loadUserInfo();

        // Logout button functionality
        btnLogout.setOnClickListener(v -> Messenger.showAlertDialog(getContext(),
                "Logout", "Are you sure you want to logout?", "Yes", "No",
                (dialogInterface, i) -> {
                    mAuth.signOut();
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                },
                (dialogInterface, i) -> dialogInterface.dismiss()
        ).show());

        // Edit Profile button functionality
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        return view;
    }

    private void loadUserInfo() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            db.readByField("userUID", currentUser.getUid(), new FirestoreCallback() {
                @Override
                public void onSuccess(Object result) {
                    UserAccount user = (UserAccount) result;
                    tvFullName.setText(user.getFirstName() + " " + user.getLastName());
                    tvGender.setText(user.getGender());
                }

                @Override
                public void onFailure(Exception e) {
                    tvFullName.setText("Error loading profile");
                    tvGender.setText("");
                }
            });
        }
    }

    private void showEditProfileDialog() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) return;

        // Fetch current user details
        db.readByField("userUID", currentUser.getUid(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                UserAccount user = (UserAccount) result;

                // Create the dialog layout
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(32, 16, 32, 16);

                // Input fields
                EditText inputFirstName = new EditText(getContext());
                inputFirstName.setHint("First Name");
                inputFirstName.setInputType(InputType.TYPE_CLASS_TEXT);
                inputFirstName.setText(user.getFirstName());
                layout.addView(inputFirstName);

                EditText inputLastName = new EditText(getContext());
                inputLastName.setHint("Last Name");
                inputLastName.setInputType(InputType.TYPE_CLASS_TEXT);
                inputLastName.setText(user.getLastName());
                layout.addView(inputLastName);

                EditText inputGender = new EditText(getContext());
                inputGender.setHint("Gender");
                inputGender.setInputType(InputType.TYPE_CLASS_TEXT);
                inputGender.setText(user.getGender());
                layout.addView(inputGender);

                // Show Material Dialog
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Edit Profile")
                        .setView(layout)
                        .setPositiveButton("Save", (dialogInterface, i) -> {
                            String updatedFirstName = inputFirstName.getText().toString().trim();
                            String updatedLastName = inputLastName.getText().toString().trim();
                            String updatedGender = inputGender.getText().toString().trim();
                            user.setFirstName(updatedFirstName);
                            user.setLastName(updatedLastName);
                            user.setGender(updatedGender);

                            db.update(currentUser.getUid(), user, new FirestoreCallback() {
                                @Override
                                public void onSuccess(Object result) {
                                    tvFullName.setText(updatedFirstName + " " + updatedLastName);
                                    tvGender.setText(updatedGender);
                                   Messenger.showAlertDialog(getContext(), "Success", "Profile updated successfully", "OK").show();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Messenger.showAlertDialog(getContext(), "Error", "Failed to update profile", "OK").show();
                                }
                            });
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }

            @Override
            public void onFailure(Exception e) {

                Messenger.showAlertDialog(getContext(), "Error", "Failed to load profile", "OK").show();}
        });
    }
}
