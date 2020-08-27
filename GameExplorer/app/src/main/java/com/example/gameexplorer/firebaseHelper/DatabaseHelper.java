package com.example.gameexplorer.firebaseHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();




    public static void saveUserName(String fullName){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("userInfo")
                .child("name")
                .setValue(fullName);
    }

    public static void saveUserEmail(String email){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("userInfo")
                .child("email address")
                .setValue(email);
    }

    public static void saveCharacter(String name, int id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("favorites")
                .child("characters")
                .child(name)
                .setValue(id);
    }

    public void saveComic(String name, String id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("wishList")
                .child("comics")
                .child(name)
                .setValue(id);
    }

    public void saveEvent(String name, String id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("favorites")
                .child("events")
                .child(name)
                .setValue(id);
    }

    public void saveSeries(String name, int id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("favorites")
                .child("series")
                .child(name)
                .setValue(id);
    }

    public void saveCreator(String name, int id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("favorites")
                .child("creators")
                .child(name)
                .setValue(id);
    }

    public void saveStories(String name, int id){
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("favorites")
                .child("Stories")
                .child(name)
                .setValue(id);
    }

    public void loadUserData(){}

    public void loadCharacter(){

    }

    //TODO:LOAD COMIC DATA
    private void loadComic(){

    }

    //TODO:LOAD EVENTS DATA
    private void loadEvent(){

    }

    //TODO:LOAD SERIES DATA
    private void loadSeries(){

    }

    //TODO:LOAD CREATORS DATA
    private void loadCreator(){

    }

    //TODO:LOAD STORIES DATA
    private void loadStories(){

    }


}
