package com.example.gameexplorer.fragment.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.SignInActivity;
import com.example.gameexplorer.activity.SignUpActivity;

public class MainFragment extends Fragment implements View.OnClickListener{
    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_main
                ,container
                , false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            View mainView = getView();

            Button btn_signUp = mainView.findViewById(R.id.btn_signUp_main);
            Button btn_signIn = mainView.findViewById(R.id.btn_signIn_main);

            btn_signUp.setOnClickListener(this);
            btn_signIn.setOnClickListener(this);
        }
    }

    //Todo:take user to sign in or sign up activity
    @Override
    public void onClick(View v) {
        Intent userChoiceIntent = null;
        if(v.getId() == R.id.btn_signUp_main){
            userChoiceIntent = new Intent(getContext(),SignUpActivity.class);

        }else if(v.getId() ==  R.id.btn_signIn_main){
           userChoiceIntent = new Intent(getContext(), SignInActivity.class);
        }
        if(userChoiceIntent != null){
            startActivity(userChoiceIntent);
        }
    }
}
