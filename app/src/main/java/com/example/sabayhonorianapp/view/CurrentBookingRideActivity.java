package com.example.sabayhonorianapp.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.adapter.CommentAdapter;
import com.example.sabayhonorianapp.callback.FirestoreCallback;
import com.example.sabayhonorianapp.model.BookRide;
import com.example.sabayhonorianapp.model.Comment;
import com.example.sabayhonorianapp.model.PostRide;
import com.example.sabayhonorianapp.model.UserAccount;
import com.example.sabayhonorianapp.repository.FirestoreRepositoryImpl;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentBookingRideActivity extends AppCompatActivity {

    private static final String TAG = "CurrentBookingRide";

    private TextView tvAuthorName, tvRideTime, tvOriginDestination, tvDescription, tvVehicleType, tvAvailableSeats, tvPrice;
    private RecyclerView rvComments;
    private TextInputLayout etComment;
    private Button btnSubmitComment;

    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;

    private FirestoreRepositoryImpl<Comment> commentRepository;
    private FirestoreRepositoryImpl<PostRide> postRideRepository;
    private FirestoreRepositoryImpl<UserAccount> userRepository;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking_ride);

        initializeRepositories();
        initializeViews();
        setupRecyclerView();

        BookRide bookRide = getIntent().getParcelableExtra("bookRide");
        if (bookRide == null) {
            Log.e(TAG, "BookRide data is null");
            finish(); // Close the activity if no data is passed
            return;
        }

        fetchPostRideData(bookRide);
    }

    private void initializeRepositories() {
        commentRepository = new FirestoreRepositoryImpl<>("comment", Comment.class);
        postRideRepository = new FirestoreRepositoryImpl<>("postRide", PostRide.class);
        userRepository = new FirestoreRepositoryImpl<>("user", UserAccount.class);
        mAuth = FirebaseAuth.getInstance();
    }

    private void initializeViews() {
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvRideTime = findViewById(R.id.tvRideTime);
        tvOriginDestination = findViewById(R.id.tvOriginDestination);
        tvDescription = findViewById(R.id.tvDescription);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        tvAvailableSeats = findViewById(R.id.tvAvailableSeats);
        tvPrice = findViewById(R.id.tvPrice);
        rvComments = findViewById(R.id.rvComments);
        etComment = findViewById(R.id.etComment);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
    }

    private void setupRecyclerView() {

    }

    private void fetchPostRideData(BookRide bookRide) {
        postRideRepository.readById(bookRide.getPostRideId(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                PostRide postRide = (PostRide) result;
                if (postRide != null) {
                    displayPostRideDetails(postRide);
                    fetchComments(postRide);
                    setupCommentSubmission(postRide);
                } else {
                    Log.e(TAG, "PostRide data is null");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error fetching PostRide data", e);
            }
        });
    }

    private void displayPostRideDetails(PostRide postRide) {
        tvAuthorName.setText("Author: " + postRide.getAuthorName());
        tvRideTime.setText("Ride Time: " + postRide.getRideTime().toDate().toString());
        tvOriginDestination.setText("Route: " + postRide.getOrigin() + " ➔ " + postRide.getDestination());
        tvDescription.setText("Description: " + postRide.getDescription());
        tvVehicleType.setText("Vehicle: " + postRide.getVehicleType());
        tvAvailableSeats.setText("Available Seats: " + postRide.getAvailableSeats());
        tvPrice.setText("Price: ₱" + postRide.getPrice());
    }

    private void fetchComments(PostRide postRide) {
        Log.d("FetchComments", "Fetching comments for PostRide with authorUID: " + postRide.getAuthorUID());

        commentRepository.readAll("authorUID", postRide.getAuthorUID(), new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                Log.d("FetchComments", "onSuccess called with result: " + result);

                if (result instanceof List<?>) {
                    List<Comment> comments = new ArrayList<>();
                    for (Object obj : (List<?>) result) {
                        if (obj instanceof Comment) {
                            comments.add((Comment) obj);
                        } else {
                            Log.w("FetchComments", "Unexpected object type in result: " + obj.getClass().getName());
                        }
                    }

                    Log.d("FetchComments", "Total comments fetched: " + comments.size());
                    commentAdapter = new CommentAdapter(comments);
                    rvComments.setLayoutManager(new LinearLayoutManager(CurrentBookingRideActivity.this));
                    rvComments.setAdapter(commentAdapter);
                } else {
                    Log.e("FetchComments", "Unexpected data type received: " + result.getClass().getName());
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FetchComments", "Failed to fetch comments", e);
            }
        });
    }



    private void setupCommentSubmission(PostRide postRide) {
        btnSubmitComment.setOnClickListener(v -> {
            String commentText = etComment.getEditText().getText().toString().trim();
            if (commentText.isEmpty()) {
                etComment.setError("Comment cannot be empty");
                return;
            }

            etComment.setError(null);
            submitComment(postRide, commentText);
        });
    }

    private void submitComment(PostRide postRide, String commentText) {
        String currentUserId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (currentUserId == null) {
            Log.e(TAG, "Current user is not logged in");
            return;
        }

        userRepository.readByField("userUID", currentUserId, new FirestoreCallback() {
            @Override
            public void onSuccess(Object result) {
                UserAccount user = (UserAccount) result;
                if (user == null) {
                    Log.e(TAG, "UserAccount data is null");
                    return;
                }

                Comment comment = new Comment(
                        user.getFirstName() + " " + user.getLastName(),
                        commentText,
                        new Timestamp(new Date()),
                        postRide.getAuthorUID()
                );

                commentRepository.create(comment, new FirestoreCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        commentList.add(comment);
                        commentAdapter.notifyItemInserted(commentList.size() - 1);
                        etComment.getEditText().setText("");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Error submitting comment", e);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Error fetching UserAccount data", e);
            }
        });
    }
}
