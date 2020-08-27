package com.example.gameexplorer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View mSignUpView;
    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get/check context for later use
        if(getContext() !=  null){
            mContext = getContext();
        }
        //check if view is null
        if(getView() != null){
            //assign view reference to a view object.
            mSignUpView = getView();

            //get button references
            Button btn_signUp = mSignUpView.findViewById(R.id.btn_signUp_su);
            Button btn_back = mSignUpView.findViewById(R.id.btn_back_su);

            //add onClick listener to buttons
            btn_signUp.setOnClickListener(this);
            btn_back.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_signUp_su){
            userInput();
        }else if(v.getId() == R.id.btn_back_su){

        }
    }

    private void userInput(){
        TextInputEditText et_firstName = mSignUpView.findViewById(R.id.et_Firstname);
        TextInputEditText et_lastName = mSignUpView.findViewById(R.id.et_lastname);
        TextInputEditText et_email = mSignUpView.findViewById(R.id.et_EmailAddress);
        TextInputEditText et_password = mSignUpView.findViewById(R.id.et_password);
        TextInputEditText et_confirmPassword = mSignUpView.findViewById(R.id.et_ConfirmPassword);

        //get user input and check if any input field is empty
        if(et_firstName.getText()!= null && et_lastName.getText() != null
                && et_email.getText() != null && et_password.getText() != null
                && et_confirmPassword.getText() != null) {

            String fName = et_firstName.getText().toString();
            String lName = et_lastName.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            String confirmPassword = et_confirmPassword.getText().toString();

            String fullName;

            if(!fName.matches("") && !lName.matches("")){
                fullName = fName +" "+lName;
            } else {
                Toast.makeText(mContext, R.string.blank_name, Toast.LENGTH_SHORT).show();
            }
        }else{

        }

    }
}
