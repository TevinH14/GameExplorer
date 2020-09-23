package com.example.gameexplorer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameexplorer.R;
import com.example.gameexplorer.adapter.GameDetailAdapter;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.model.GameDetail;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.networkHelper.GameDetailTask;
import com.squareup.picasso.Picasso;
import android.transition.TransitionManager;
import android.widget.Toast;

public class GameDetailFragment extends Fragment implements GameDetailTask.OnGamesFinished,
        GameDetailAdapter.OnItemClicked, View.OnClickListener {

    private static String selectedGame;
    private GameDetail mGame;
    private boolean mIsVideo = false;

    // get/set game data to get detail in activity created
    public static GameDetailFragment newInstance(String game) {
        
        Bundle args = new Bundle();
        selectedGame = game;
        GameDetailFragment fragment = new GameDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_game_details,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GameDetailTask gdt = new GameDetailTask(this);
        gdt.execute("https://api.rawg.io/api/games/"+selectedGame);

    }

    @Override
    public void onGameDetailPost(GameDetail games) {
        if(getView() != null && selectedGame != null
                && getActivity() != null && getContext() != null && games != null){
            mGame = games;
            TextView tv_title = getView().findViewById(R.id.tv_title_gameDetail);
            tv_title.setText(games.getTitle());
            TextView tv_metaRating = getView().findViewById(R.id.tv_metRating_gd);
            int criticRatingNum = games.getCriticRating();
            if(criticRatingNum != -1){
                String criticRating = String.valueOf(criticRatingNum);
                tv_metaRating.setText(criticRating);
                if(criticRatingNum >= 75){
                    tv_metaRating.setBackgroundResource(R.color.greenRatingColor);

                }else if(criticRatingNum >= 50){
                    tv_metaRating.setBackgroundResource(R.color.yellowRatingColor);
                    tv_metaRating.setTextColor(Color.BLACK);
                }
                else {
                    tv_metaRating.setBackgroundResource(R.color.redRatingColor);
                }
            }

            tv_title.setText(games.getTitle());


            setMainDisplay(-1);
            setUpImageVideoScroll();

            Button btn_description = getView().findViewById(R.id.btn_description_dd);
            btn_description.setOnClickListener(this);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_add) {
            RealTimeDatabaseHelper.saveGame(mGame.getTitle(),mGame.getBackgroundImage(),mGame.getSlugName());
            Toast.makeText(getContext(), R.string.game_added,
                    Toast.LENGTH_LONG).show();
            return true;
        }else if (item.getItemId() == android.R.id.home  && getActivity() != null) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(final int position, boolean isVideo) {
        mIsVideo = isVideo;
        if(!mIsVideo){
            setImageDisplay(position);
        }else {
            setMainDisplay(position);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_description_dd && getView() != null){
            View gdView = getView();
                CardView cv = gdView.findViewById(R.id.cv_description_gd);
            ConstraintLayout cl = gdView.findViewById(R.id.cl_textBox_gd);
            if(cl.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(cv,new AutoTransition());
                cl.setVisibility(View.GONE);
            }else{
                TextView tv_description = gdView.findViewById(R.id.tv_description_gd);
                TransitionManager.beginDelayedTransition(cv,new AutoTransition());
                tv_description.setText(mGame.getDescription());
                cl.setVisibility(View.VISIBLE);
            }
        }
    }

    //display image in alert dialog
    private void setImageDisplay(int pos){
        AlertDialog.Builder ImageDialog = new AlertDialog.Builder(getContext());
        ImageDialog.setTitle("Image");
        final ImageView showImage = new ImageView(getContext());
        Picasso
                .get()
                .load(mGame.getScreenShotsUrl()[pos])
                .resize(800,500)
                .into(showImage);
        ImageDialog.setView(showImage);
        ImageDialog.setNegativeButton("Close", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int arg1)
            {
                dialog.cancel();
            }
        });
        ImageDialog.show();
    }

    //set up fragment view
    private void setMainDisplay(int pos){
        if(getActivity() != null) {
            if (!mIsVideo) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_image_video_container, BackgroundImageFragment
                                .newInstance(mGame.getBackgroundImage()))
                        .commit();
            } else if(pos != -1){
                FrameLayout fl_IV = getView().findViewById(R.id.fl_image_video_container);

                // setVideoFragment to display
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_gameVideo, VideoPlayerFragment
                                .newInstance(mGame.getVideoUrl()[pos]))
                        .commit();

            }
        }
    }

    //set up image and video horizontal scroll
    private void setUpImageVideoScroll(){
        if(getView() != null && getContext() != null) {

            //set up screenShots in recycler view
            LinearLayoutManager imageManager = new LinearLayoutManager(getContext().getApplicationContext());
            imageManager.setOrientation(LinearLayoutManager.HORIZONTAL);


            RecyclerView screenshotView = getView().findViewById(R.id.rv_imageGallery);
            screenshotView.setLayoutManager(imageManager);
            screenshotView.setItemAnimator(new DefaultItemAnimator());

            GameDetailAdapter screenShotAdapter = new GameDetailAdapter(mGame.getScreenShotsUrl(),false);
            screenshotView.setAdapter(screenShotAdapter);
            screenShotAdapter.setOnClick(this);

            RecyclerView videoThumbnailView = getView().findViewById(R.id.rv_videoGallery);
            if(mGame.getMoviesPreviews() != null) {
                //set up video preview image in recycler view
                LinearLayoutManager videoManager = new LinearLayoutManager(getContext().getApplicationContext());
                videoManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                videoThumbnailView.setLayoutManager(videoManager);
                videoThumbnailView.setItemAnimator(new DefaultItemAnimator());

                GameDetailAdapter thumbnailAdapter = new GameDetailAdapter(mGame.getMoviesPreviews(), true);
                videoThumbnailView.setAdapter(thumbnailAdapter);
                thumbnailAdapter.setOnClick(this);
            }else {
                getView().findViewById(R.id.tv_video_gd).setVisibility(View.GONE);
                videoThumbnailView.setVisibility(View.GONE);
            }
        }
    }
}
