package com.example.sabayhonorianapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.Route;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.DateFormatter;
import com.example.sabayhonorianapp.util.Messenger;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Point;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.autocomplete.PlaceAutocompleteOptions;
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.SearchResultsView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

public class CreateRideActivity extends AppCompatActivity {

    private TextInputEditText etRideDate, etRideTime, etRideEndTime, etOrigin, etDestination, etAvailableSet, etDescription, etFare;
    private Spinner etVehicleType;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private Route route;
    FirebaseAuth mAuth;
    private FirestoreRepositoryImpl<UserAccount> userAccountFirestoreRepository;
    private GenericService<UserAccount> userAccountGenericService;
    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private GenericService<PostRide> postRideService;
    private Button btnAddRide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);


        userAccountFirestoreRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        userAccountGenericService = new GenericService<>(userAccountFirestoreRepository);
        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        postRideService = new GenericService<>(postRideRepository);


        btnAddRide = findViewById(R.id.btnAddRide);

        btnAddRide.setOnClickListener(this::saveAction);

        etRideDate = findViewById(R.id.etRideDate);
        etRideTime = findViewById(R.id.etRideTime);
        etRideEndTime = findViewById(R.id.etRideEndTime);
        etOrigin = findViewById(R.id.etOrigin);
        etFare = findViewById(R.id.etFare);

        etVehicleType = findViewById(R.id.etVehicleType);
        mAuth = FirebaseAuth.getInstance();

        etDestination = findViewById(R.id.etDestination);

        etRideDate.setOnClickListener(view -> showDatePicker(etRideDate));
        etRideTime.setOnClickListener(view -> showTimePicker(etRideTime));
        etRideEndTime.setOnClickListener(view -> showTimePicker(etRideEndTime));

        etAvailableSet = findViewById(R.id.etAvailableSeats);
        etDescription = findViewById(R.id.etDescription);

        if (getIntent().hasExtra("route")){
            route = getIntent().getParcelableExtra("route" );
            etOrigin.setText(route.getStartingName());
            etDestination.setText(route.getEndingName());

        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etVehicleType.setAdapter(adapter);

    }

    public void saveAction(View view){

        String description = etDescription.getText().toString();
        String postDateStr = etRideDate.getText().toString();
        String rideTimeStr = etRideTime.getText().toString();
        String rideEndTimeStr = etRideEndTime.getText().toString();
        String destination = etDestination.getText().toString();
        String origin = etOrigin.getText().toString();
        String vehicleType = etVehicleType.getSelectedItem().toString();
        String fare = etFare.getText().toString();
        int availableSeats = Integer.parseInt(etAvailableSet.getText().toString());

        if (description.isEmpty() || postDateStr.isEmpty() || rideTimeStr.isEmpty() || rideEndTimeStr.isEmpty() || destination.isEmpty() || origin.isEmpty() || vehicleType.isEmpty() || availableSeats == 0) {
            Messenger.showAlertDialog(this, "Error", "Please fill in all fields", "Ok").show();
            return;
        }

        Date postDate = DateFormatter.parseDate(postDateStr);
        Timestamp rideTime = null, rideEndTime = null;
        try {
            Date rideTimeParsed = timeFormat.parse(rideTimeStr);
            Date rideEndTimeParsed = timeFormat.parse(rideEndTimeStr);
            rideTime = new Timestamp(rideTimeParsed);
            rideEndTime = new Timestamp(rideEndTimeParsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PostRide postRide = new PostRide();
        postRide.setDescription(description);

        postRide.setRideTime(rideTime);
        postRide.setRideEnd(rideEndTime);
        postRide.setDestination(destination);
        postRide.setOrigin(origin);
        postRide.setVehicleType(vehicleType);
        postRide.setAvailableSeats(availableSeats);
        postRide.setStatus("PENDING");
        postRide.setAuthorUID(mAuth.getCurrentUser().getUid());
        postRide.setPostTime(new Timestamp(new Date()));
        postRide.setPostDate(new Date());
        postRide.setRideDate(postDate);
        postRide.setOriginCoordination(route.getStartingCoordination());
        postRide.setDestinationCoordination(route.getEndingCoordination());
       postRide.setPrice(Double.parseDouble(fare));


        userAccountGenericService.readItemByField("userUID", mAuth.getCurrentUser().getUid(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {

                UserAccount userAccount = (UserAccount) result;
                postRide.setAuthorName(userAccount.getFirstName() + " " + userAccount.getLastName());
                postRideService.createItem(postRide, new FirestoreCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        Messenger.showAlertDialog(CreateRideActivity.this, "Success", "Ride published successfully", "Ok").show();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Messenger.showAlertDialog(CreateRideActivity.this, "Error", "Failed to publish ride", "Ok").show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Messenger.showAlertDialog(CreateRideActivity.this, "Error", "Failed to get user information", "Ok").show();
            }
        });


    }



    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            editText.setText(timeFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
