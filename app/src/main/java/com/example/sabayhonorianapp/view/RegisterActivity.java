package com.example.sabayhonorianapp.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.DateFormatter;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {


    private Spinner spGender;


    private Button btnRegister;
    private Button btnDatePicker;

    private FirestoreRepositoryImpl<UserAccount> userAccountFirestoreRepository;
    GenericService<UserAccount> userAccountGenericService;

    private FirebaseAuth mAuth;


    private TextInputLayout tilFirstName, tilLastName, tilMiddleName, tilBirtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        spGender = findViewById(R.id.spGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);



        tilFirstName = findViewById(R.id.tilFirstName);
        tilMiddleName = findViewById(R.id.tilMiddleName);
        tilLastName = findViewById(R.id.tilLastName);
        tilBirtDate = findViewById(R.id.tilBirthdate);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::registerAction);
        btnDatePicker = findViewById(R.id.btnSelectDate);
        btnDatePicker.setOnClickListener(this::datePickerAction);

        userAccountFirestoreRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        userAccountGenericService = new GenericService<>(userAccountFirestoreRepository);

        mAuth = FirebaseAuth.getInstance();

    }

    private void datePickerAction(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    String firebaseFormattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd")
                            .format(calendar.getTime());
                    tilBirtDate.getEditText().setText(firebaseFormattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }


    private void registerAction(View view) {


        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName(tilFirstName.getEditText().getText().toString());
        userAccount.setMiddleName(tilMiddleName.getEditText().getText().toString());
        userAccount.setLastName(tilLastName.getEditText().getText().toString());
        userAccount.setGender(spGender.getSelectedItem().toString());

        Date date = DateFormatter.parseDate(tilBirtDate.getEditText().getText().toString());
        userAccount.setBirthDate(date);

        userAccount.setUserUID(mAuth.getCurrentUser().getUid());

        userAccountGenericService.createItem(userAccount, new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                Messenger.showAlertDialog(RegisterActivity.this, "Register", "User Account has been created!", "Ok").show();
                startActivity(new Intent(RegisterActivity.this, HeroActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Messenger.showAlertDialog(RegisterActivity.this, "Register", "Failed to create user account!", "Ok").show();
            }
        });


    }
}