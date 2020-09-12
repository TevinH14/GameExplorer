package com.example.gameexplorer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;
import com.squareup.picasso.Picasso;

public class BackgroundImageFragment extends Fragment {
    private static String mImageUrl;
    public static BackgroundImageFragment newInstance(String imageUrl) {
        mImageUrl = imageUrl;
        Bundle args = new Bundle();

        BackgroundImageFragment fragment = new BackgroundImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_view,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            ImageView iv = getView().findViewById(R.id.iv_gameImage_bif);
            Picasso
                    .get()
                    .load(mImageUrl)
                    .resize(iv.getWidth(),200)
                    .into(iv);
        }
    }
}
