package com.example.gameexplorer.firebaseHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class FirebaseStorageHelper {
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static final StorageReference mStorageReference = mStorage.getReference();

    private static UserImageFinisher mImageFinisher = null;

    //interface to provide drawer header image
    public interface UserImageFinisher{
        void onGotImage(Bitmap bitmap);
    }

    //assign interface activity thats using it.
    public FirebaseStorageHelper(UserImageFinisher mImageFinisher) {
        FirebaseStorageHelper.mImageFinisher = mImageFinisher;
    }

    //save image file using image Uri
    //use user uid to save users image.
    public static void uploadImage(Uri filePath){
        if(filePath != null){
            // Defining the child of storageReference
            StorageReference ref
                    = mStorageReference
                    .child(
                            "images/"
                                    + UserAuthenticationHelper.getUserUid());

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    //get user profile image using user uid
    public static void downloadImage(){
        try {
            StorageReference imageRef = mStorage.getReferenceFromUrl(
                    "gs://game-explorer-758a7.appspot.com")
                    .child("images/"
                            + UserAuthenticationHelper.getUserUid());

            final File file = File.createTempFile("image", "jpg");
            imageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap  = BitmapFactory.decodeFile(file.getAbsolutePath());
                    mImageFinisher.onGotImage(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    mImageFinisher.onGotImage(null);

                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
