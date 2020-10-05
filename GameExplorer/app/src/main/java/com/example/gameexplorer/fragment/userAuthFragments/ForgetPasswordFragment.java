package com.example.gameexplorer.fragment.userAuthFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.UserAuthenticationHelper;
import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordFragment extends Fragment implements View.OnClickListener {
    public static ForgetPasswordFragment newInstance() {

        Bundle args = new Bundle();

        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            Button btn_continue = getView().findViewById(R.id.btn_continue_fp);
            btn_continue.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_continue_fp && getView() != null) {
            View forgetPasswordView = getView();

            TextInputEditText et_email = forgetPasswordView.findViewById(R.id.et_forgetPassword);
            if (et_email.getText() != null) {

                final String email = et_email.getText().toString();

                if(!email.matches("")) {

                    //alert email has been sent
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                    alertDialog.setTitle(R.string.email_sent);
                    alertDialog.setTitle(R.string.check_email);
                    alertDialog.setIcon(R.drawable.ic_announcement_24dp);

                    alertDialog.setPositiveButton(R.string.okay,
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserAuthenticationHelper.sendForgotPassword(email);
                            if(getActivity() != null){
                                getActivity().finish();
                            }
                        }
                    });

                    //show alert dialog
                    alertDialog.show();
                }else{
                    Toast.makeText(getContext(), R.string.blank_email, Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getContext(), R.string.blank_email, Toast.LENGTH_LONG).show();
            }
        }
    }
}
