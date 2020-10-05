package com.example.gameexplorer.firebaseHelper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gameexplorer.model.Games;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class RealTimeDatabaseHelper{
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String mCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private static FavoriteFinished mOnFavoriteFinished = null;
    private static GameExistFinished mOnExistFinished = null;
    private static NameFinished mOnNameFinished = null;

    public interface FavoriteFinished{
        void onFavoritePost(ArrayList<Games> _gamesList);
    }
    public interface GameExistFinished{
        void onExistPost(boolean hasGame);
    }
    public interface NameFinished{
        void OnNamePost(String _name);
    }

    //method overloading interfaces
    public RealTimeDatabaseHelper(GameExistFinished mOnExistFinished) {
        RealTimeDatabaseHelper.mOnExistFinished = mOnExistFinished;
    }
    public RealTimeDatabaseHelper(FavoriteFinished mOnFavoriteFinished) {
        RealTimeDatabaseHelper.mOnFavoriteFinished = mOnFavoriteFinished;
    }
    public RealTimeDatabaseHelper(NameFinished mOnNameFinished){
        RealTimeDatabaseHelper.mOnNameFinished = mOnNameFinished;
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

    public static void saveGame(String name, String image, String slug,int id){
        String idString = String.valueOf(id);

        final HashMap<String,Object> gameData = new HashMap<>();
        gameData.put("name",name);
        gameData.put("slug",slug);
        gameData.put("image_url",image);

        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("Favorites")
                .child(idString)
                .setValue(gameData);
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
                if(gameList != null) {
                    mOnFavoriteFinished.onFavoritePost(gameList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(postListener);

    }

    public static void loadName(){
        DatabaseReference reference = mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("/userInfo")
                .child("name");
        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name =  dataSnapshot.getValue(String.class);
                mOnNameFinished.OnNamePost(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(postListener);
    }

    public static void removeGame(int id){
        String idString = String.valueOf(id);
        mDatabase
                .child("users")
                .child(mCurrentUser)
                .child("Favorites")
                .child(idString).removeValue();
    }

    public static void checkIfGameExist(final int id){
        DatabaseReference reference = mDatabase.child("users").child(mCurrentUser+"/Favorites");
        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String idString = String.valueOf(id);
                boolean doesExist = false;
                if (snapshot.hasChild(idString)) {
                    doesExist = true;
                }
                mOnExistFinished.onExistPost(doesExist);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        reference.addListenerForSingleValueEvent(postListener);
    }

}
