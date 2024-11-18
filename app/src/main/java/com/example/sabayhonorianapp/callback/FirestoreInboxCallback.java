package com.example.sabayhonorianapp.callback;

public interface FirestoreInboxCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}
