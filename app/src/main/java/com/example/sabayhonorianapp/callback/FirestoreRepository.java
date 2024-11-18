package com.example.sabayhonorianapp.callback;

public interface FirestoreRepository<T> {
    void create(T item, FirestoreCallback callback);
    void readAll(FirestoreCallback callback);
    void update(String id, T item, FirestoreCallback callback);
    void delete(String id, FirestoreCallback callback);
    void readById(String id, final FirestoreCallback callback);
    void readByField(String field, String value, final FirestoreCallback callback);

}