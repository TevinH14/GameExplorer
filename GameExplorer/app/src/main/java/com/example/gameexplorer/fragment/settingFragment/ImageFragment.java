package com.example.gameexplorer.fragment.settingFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.HomeActivity;
import com.example.gameexplorer.firebaseHelper.FirebaseStorageHelper;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageFragment extends Fragment implements View.OnClickListener{
    private final int PICK_IMAGE_REQUEST = 22;
    private CircleImageView mCircleImageView;
    private Uri mFilePath;
    private static boolean mComingFromSU;
    public static ImageFragment newInstance(boolean comingFromSU) {
        mComingFromSU = comingFromSU;
        return new ImageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_image,container,false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home  && getActivity() != null) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            View v = getView();
            Button btn_selectImage = v.findViewById(R.id.btn_select_ui);
            Button btn_skip = v.findViewById(R.id.btn_skip_ui);
            Button btn_continue = v.findViewById(R.id.btn_continue_ui);
            mCircleImageView = v.findViewById(R.id.iv_userImage_ui);

            if (mComingFromSU) {
                btn_selectImage.setOnClickListener(this);
                btn_continue.setOnClickListener(this);
            }
            else{
                btn_continue.setVisibility(View.GONE);
                btn_skip.setText(R.string.done);
            }
            btn_skip.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        if(getActivity() != null) {
            if (v.getId() == R.id.btn_select_ui) {
                selectImage();
            } else if (v.getId() == R.id.btn_skip_ui) {
                if (mComingFromSU) {
                    Intent homeIntent = new Intent(getContext(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    getActivity().finish();
                }
            } else if (v.getId() == R.id.btn_continue_ui) {
                FirebaseStorageHelper.uploadImage(mFilePath);
                Intent homeIntent = new Intent(getContext(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(getActivity() != null) {
            if (requestCode == PICK_IMAGE_REQUEST
                    && resultCode == -1
                    && data != null
                    && data.getData() != null) {
                mFilePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(
                                    getActivity().getContentResolver(),
                                    mFilePath);
                    mCircleImageView.setImageBitmap(bitmap);

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,"Select image from here"),
                PICK_IMAGE_REQUEST);
    }
}
