package com.example.gameexplorer.firebaseHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserAuthenticationHelper{
    private static final String TAG = "FIREBASE_TEST";
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public static void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password);
    }

    public static boolean signInUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password);
        return checkUserStatus();
    }

    public static void signOutUser(){
       mAuth.signOut();
    }

    public static boolean checkUserStatus(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public static void sendForgotPassword(String email){
        mAuth.sendPasswordResetEmail(email);
    }


    public static String getUserUid(){
        if(mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public static String verifyUserEmail(String email_one,String email_two){
        ArrayList<String> userData = new ArrayList<>();
        boolean verifyEmailError = false;
        //check user email matches
        if(email_one.equals(email_two)){
            return email_two;
        }else{
            return null;
        }
    }

    public static String verifyUserPassword(String password_one,String password_two){
        ArrayList<String> userData = new ArrayList<>();
        boolean verifyEmailError = false;
        //check user email matches
        if(password_one.equals(password_two)){
            return password_two;
        }else{
            return null;
        }
    }

}
