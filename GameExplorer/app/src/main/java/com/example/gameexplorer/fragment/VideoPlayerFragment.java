package com.example.gameexplorer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gameexplorer.R;

public class VideoPlayerFragment extends Fragment implements View.OnClickListener {

    //private ConstraintLayout mVideoControllerView;
    private static String mUrl;
    public static VideoPlayerFragment newInstance(String url) {
        mUrl = url;
        Bundle args = new Bundle();

        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_viewer,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null){
            View v = getView();
           // mVideoControllerView = v.findViewById(R.id.cl_videoControl);

            try {

                VideoView videoView = v.findViewById(R.id.vv_gameMovie);
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                Uri video = Uri.parse(mUrl);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video);
                videoView.start();
            } catch (Exception e) {
                // TODO: handle exception
                Toast.makeText(getContext(), "Error connecting", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
//
//        if(v.getId() == R.id.vv_gameMovie){
//
//        }
    }
}
