package com.example.sabayhonorianapp.callback;

public interface FirestoreCallback {
    void onSuccess(Object result);
    void onFailure(Exception e);
}