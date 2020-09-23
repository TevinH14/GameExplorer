package com.example.gameexplorer.firebaseHelper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gameexplorer.model.GameDetail;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.GameDetailTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealTimeDatabaseHelper{
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static FavoriteFinished mOnFinishedInterface = null;

    public interface FavoriteFinished{
        void onFavoritePost(ArrayList<Games> _gamesList);
    }
    public RealTimeDatabaseHelper(FavoriteFinished mOnFinishedInterface) {
        RealTimeDatabaseHelper.mOnFinishedInterface = mOnFinishedInterface;
    }

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

    public static void saveUserData(String name, String email){
        mDatabase
                .child("Accounts")
                .child(name)
                .setValue(email);
    }

    public static void saveGame(String name, String image, String slug){
        final HashMap<String,Object> gameData = new HashMap<>();
        gameData.put("name",name);
        gameData.put("slug",slug);
        gameData.put("image_url",image);

        DatabaseReference reference = mDatabase.child("users").child(mCurrentUser+"/Favorites");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                int i = (int) dataSnapshot.getChildrenCount();
                mDatabase
                        .child("users")
                        .child(mCurrentUser)
                        .child("Favorites")
                        .child("item" + i)
                        .setValue(gameData);
                Log.i("count:"," " + i);
                loadGame();
                // ...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(postListener);

    }
    public static void loadGame() {
        DatabaseReference reference = mDatabase.child("users").child(mCurrentUser+"/Favorites");
        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Games> gameList = new ArrayList<>();
                for (DataSnapshot c : dataSnapshot.getChildren()){

                    String name = (String) c.child("name").getValue();
                    String slug = (String) c.child("slug").getValue();
                    String imageUrl = (String) c.child("image_url").getValue();

                    gameList.add(new Games(name,slug,imageUrl));
                }
                if(gameList.size() > 0 || gameList != null) {
                    mOnFinishedInterface.onFavoritePost(gameList);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(postListener);

    }

}
