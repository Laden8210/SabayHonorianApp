package com.example.sabayhonorianapp.fragment.bottomsheet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.example.sabayhonorianapp.service.GenericService;
import com.example.sabayhonorianapp.util.DateFormatter;
import com.example.sabayhonorianapp.util.Messenger;
import com.example.sabayhonorianapp.view.CreateRideActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class PublishRideBottomSheet extends BottomSheetDialogFragment {

    private TextInputEditText etDescription, etRideDate, etRideTime, etRideEndTime, etOrigin, etAvailableSeats;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private GenericService<PostRide> postRideService;

    private Spinner etVehicleType;
    private TextInputEditText etDestination;
    private FirestoreRepositoryImpl<UserAccount> userAccountFirestoreRepository;
    private GenericService<UserAccount> userAccountGenericService;
    private PlaceAutocomplete placeAutocomplete;


    private SearchResultsView searchResultsView;
    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private boolean ignoreNextQueryUpdate = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bs_add_ride, container, false);

        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        postRideService = new GenericService<>(postRideRepository);

        userAccountFirestoreRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        userAccountGenericService = new GenericService<>(userAccountFirestoreRepository);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));

        searchResultsView = view.findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration(new CommonSearchViewConfiguration()));
        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView, placeAutocomplete, LocationEngineProvider.getBestLocationEngine(getContext()));

        etDescription = view.findViewById(R.id.etDescription);
        etRideDate = view.findViewById(R.id.etRideDate);
        etRideTime = view.findViewById(R.id.etRideTime);
        etRideEndTime = view.findViewById(R.id.etRideEndTime);
        etDestination = view.findViewById(R.id.etDestination);
        etOrigin = view.findViewById(R.id.etOrigin);
        etVehicleType = view.findViewById(R.id.etVehicleType);
        etAvailableSeats = view.findViewById(R.id.etAvailableSeats);

        initSearchOrigin();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.vehicle_type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etVehicleType.setAdapter(adapter);

        ArrayAdapter<CharSequence> vadapter = ArrayAdapter.createFromResource(getContext(),
                R.array.destination_options, android.R.layout.simple_spinner_item);
        vadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




        etRideDate.setOnClickListener(v -> showDatePicker());
        etRideTime.setOnClickListener(v -> showTimePicker(etRideTime));
        etRideEndTime.setOnClickListener(v -> showTimePicker(etRideEndTime));

        Button submitButton = view.findViewById(R.id.btnAddRide);
        submitButton.setOnClickListener(v -> {
            String description = etDescription.getText().toString();
            String postDateStr = etRideDate.getText().toString();
            String rideTimeStr = etRideTime.getText().toString();
            String rideEndTimeStr = etRideEndTime.getText().toString();
            String destination = etDestination.getText().toString();
            String origin = etOrigin.getText().toString();
            String vehicleType = etVehicleType.getSelectedItem().toString();
            int availableSeats = Integer.parseInt(etAvailableSeats.getText().toString());

            if (description.isEmpty() || postDateStr.isEmpty() || rideTimeStr.isEmpty() || rideEndTimeStr.isEmpty() || destination.isEmpty() || origin.isEmpty() || vehicleType.isEmpty() || availableSeats == 0) {
                Messenger.showAlertDialog(getActivity(), "Error", "Please fill in all fields", "Ok").show();
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


            postRide.setDestination(destination);
            postRide.setOrigin(origin);
            postRide.setVehicleType(vehicleType);
            postRide.setAvailableSeats(availableSeats);
            postRide.setStatus("PENDING");
            postRide.setAuthorUID(mAuth.getCurrentUser().getUid());
            postRide.setPostTime(new Timestamp(new Date()));
            postRide.setPostDate(new Date());
            postRide.setRideDate(postDate);



            userAccountGenericService.readItemByField("userUID", mAuth.getCurrentUser().getUid(), new FirestoreCallback() {
                @Override
                public void onSuccess(Object result) {

                    UserAccount userAccount = (UserAccount) result;
                    postRide.setAuthorName(userAccount.getFirstName() + " " + userAccount.getLastName());
                    postRideService.createItem(postRide, new FirestoreCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            Messenger.showAlertDialog(getActivity(), "Success", "Ride published successfully", "Ok").show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Messenger.showAlertDialog(getActivity(), "Error", "Failed to publish ride", "Ok").show();
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    Messenger.showAlertDialog(getActivity(), "Error", "Failed to get user information", "Ok").show();
                }
            });




        });

        return view;
    }

    private void initSearchOrigin(){
        etOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ignoreNextQueryUpdate) {
                    ignoreNextQueryUpdate = false;
                } else {

                    placeAutocompleteUiAdapter.search(etOrigin.getText().toString(), new Continuation<Unit>() {
                        @NonNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NonNull Object o) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    searchResultsView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            // Change the format here to match your expected input
            String date = selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay); // Format as yyyy-MM-dd
            etRideDate.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }


    private void showTimePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute); // Format your time here
            editText.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }
}
