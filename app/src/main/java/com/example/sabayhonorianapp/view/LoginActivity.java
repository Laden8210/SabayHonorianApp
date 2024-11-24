package com.example.sabayhonorianapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.tilEmail);
        password = findViewById(R.id.tilPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::loginAction);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::registerUser);

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser(View view) {
        String emailInput = email.getEditText().getText().toString().trim();
        String passwordInput = password.getEditText().getText().toString().trim();

        if (!validateEmail(emailInput) || !validatePassword(passwordInput)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                Messenger.showAlertDialog(this, "Registration successful", "You have successfully registered", "OK").show();

                mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(signInTask -> {
                    if (signInTask.isSuccessful()) {

                        startActivity(new Intent(this, RegisterActivity.class));
                        finish();
                    } else {
                        Messenger.showAlertDialog(this, "Login failed", "Unable to log in after registration", "OK").show();
                    }
                });
            } else {
                Messenger.showAlertDialog(this, "Registration failed", "Registration failed", "OK").show();
            }
        });

    }

    private void loginAction(View view) {
        String emailInput = email.getEditText().getText().toString().trim();
        String passwordInput = password.getEditText().getText().toString().trim();

        if (!validateEmail(emailInput) || !validatePassword(passwordInput)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {

                Messenger.showAlertDialog(this, "Login failed", "Login failed", "OK").show();
                updateUI(null);
            }
        });
    }

    private boolean validateEmail(String emailInput) {
        if (emailInput.isEmpty()) {
            showError("Email is required", "Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            showError("Please provide a valid email", "Invalid email format");
            return false;
        } else if (!emailInput.contains("dhvsu.edu.ph")) {
            showError("Please provide a valid dhvsu email", "Invalid domain");
            return false;
        } else {

            return true;
        }
    }

    private void showError(String alertMessage, String errorMessage) {
        Messenger.showAlertDialog(this, "Validation failed", alertMessage, "OK").show();

    }

    private boolean validatePassword(String passwordInput) {
        if (passwordInput.isEmpty()) {
            showPasswordError("Password is required", "Password is empty");
            return false;
        } else if (passwordInput.length() < 6) {
            showPasswordError("Password must be at least 6 characters", "Password too short");
            return false;
        } else {
            return true;
        }
    }

    private void showPasswordError(String alertMessage, String errorMessage) {
        Messenger.showAlertDialog(this, "Validation failed", alertMessage, "OK").show();

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

        }
    }
}
