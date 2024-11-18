package com.example.sabayhonorianapp.service;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sabayhonorianapp.callback.ImageCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageService {


    public static void uploadImageToFirebase(Uri uri, ImageCallback callback) {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/" + System.currentTimeMillis() + ".jpg");

            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String downloadUrl = downloadUri.toString();
                                    callback.onSuccess(downloadUrl);
                                    Log.d("Firebase", "Upload successful. Download URL: " + downloadUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Upload failed", e);
                            callback.onFailure(e);
                        }
                    });
        }
    }
}
