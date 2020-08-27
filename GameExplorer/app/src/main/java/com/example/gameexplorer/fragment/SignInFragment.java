package com.example.gameexplorer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.HomeActivity;
import com.example.gameexplorer.firebaseHelper.UserAuthenticationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class SignInFragment extends Fragment implements View.OnClickListener {
    private View mSignInView;

    public static SignInFragment newInstance() {

        Bundle args = new Bundle();

        SignInFragment fragment = new SignInFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(getView() != null) {
            mSignInView = getView();
            mSignInView.findViewById(R.id.btn_signIn_si).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_signIn_si){
            UserAuthenticationHelper.signOutUser();
            TextInputEditText et_email = mSignInView.findViewById(R.id.et_email_si);
            TextInputEditText et_password = mSignInView.findViewById(R.id.et_password_si);

            if(et_email.getText() != null && et_password.getText() != null){
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(!email.matches("") && !password.matches("")){
                    boolean userStatus = UserAuthenticationHelper.signInUser(email, password);
                    if (userStatus) {
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), R.string.incorrect_email_or_password,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), R.string.nothing_blank, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}