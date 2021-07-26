package com.example.gameexplorer.fragment.gamesFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.GameDetailActivity;
import com.example.gameexplorer.activity.HomeActivity;
import com.example.gameexplorer.adapter.ViewPagerAdapter;
import com.example.gameexplorer.adapter.gameAdapters.GameDetailAdapter;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.fragment.BackgroundImageFragment;
import com.example.gameexplorer.fragment.VideoPlayerFragment;
import com.example.gameexplorer.model.GameDetail;
import com.squareup.picasso.Picasso;
import android.transition.TransitionManager;
import android.widget.Toast;

public class GameDetailFragment extends Fragment implements
        GameDetailAdapter.OnItemClicked, View.OnClickListener {

    private static String selectedGame;
    private static GameDetail mGame;
    private static boolean mIsSaved;
    private boolean mIsVideo = false;
    private static boolean mIsFromFavorite = false;

    // get/set game data to get detail in activity created
    public static GameDetailFragment newInstance(GameDetail game, boolean isSaved, boolean isFromFavorite) {
        mGame = game;
        mIsSaved = isSaved;
        mIsFromFavorite = isFromFavorite;

        return new GameDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_game_details,container,false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.menu_add || item.getItemId() == R.id.menu_delete) {
            if (item.getItemId() == R.id.menu_add) {
                RealTimeDatabaseHelper.saveGame(mGame.getTitle(), mGame.getBackgroundImage(),
                        mGame.getSlugName(), mGame.getGameId());
                Toast.makeText(getContext(), R.string.game_added,
                        Toast.LENGTH_LONG).show();
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                RealTimeDatabaseHelper.removeGame(mGame.getGameId());
            }
            if(getActivity() != null){
                ((GameDetailActivity)getActivity()).replaceFragments();
            }
        }
        else if (item.getItemId() == android.R.id.home  && getActivity() != null) {
            getActivity().finish();
//            if(!mIsFromFavorite) {
//                getActivity().finish();
//            }
//            else{
//                Intent favoriteIntent = new Intent(getContext(), HomeActivity.class);
//                startActivity(favoriteIntent);
//            }s
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!mIsSaved) {
            inflater.inflate(R.menu.game_detail_add, menu);
        }
        else {
            inflater.inflate(R.menu.game_detail_delete, menu);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null && getActivity() != null && getContext() != null && mGame != null){


            //set game title
            TextView tv_title = getView().findViewById(R.id.tv_title_gameDetail);
            tv_title.setText(mGame.getTitle());

            //set game background image
            ImageView iv = getView().findViewById(R.id.iv_gameImage_gd);
            Picasso
                    .get()
                    .load(mGame.getBackgroundImage())
                    .resize(300,iv.getHeight())
                    .into(iv);

            //Set number rating
            TextView tv_metaRating = getView().findViewById(R.id.tv_metRating_gd);
            int criticRatingNum = mGame.getCriticRating();
            if(criticRatingNum != -1){
                String criticRating = String.valueOf(criticRatingNum);
                tv_metaRating.setText(criticRating);

                //set background color based on rating
                if(criticRatingNum >= 75){
                    tv_metaRating.setBackgroundResource(R.color.greenRatingColor);

                }
                else if(criticRatingNum >= 50){
                    tv_metaRating.setBackgroundResource(R.color.yellowRatingColor);
                    tv_metaRating.setTextColor(Color.BLACK);
                }
                else {
                    tv_metaRating.setBackgroundResource(R.color.redRatingColor);
                }
            }
            if(mGame.getMoviesPreviews() != null && mGame.getMoviesPreviews().length > 0){
                setMainDisplay(-1);

            }
            setUpImageVideoScroll();

            Button btn_description = getView().findViewById(R.id.btn_description_dd);
            btn_description.setOnClickListener(this);

            setViewPagers();
        }

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
                        .replace(R.id.fl_gameVideo, BackgroundImageFragment
                                .newInstance(mGame.getMoviesPreviews()[0]))
                        .commit();
            } else if(pos != -1){
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
                getView().findViewById(R.id.fl_gameVideo).setVisibility(View.GONE);

            }
        }
    }

    private void setViewPagers(){
        if(getView() != null) {
            //pull games data out and provided to the viewPager
            ViewPager viewPager = getView().findViewById(R.id.vp_seriesGames_gd);

            if(mGame.getSeriesGames() != null) {
                int length = mGame.getSeriesGames().length;
                String[] st = new String[length];
                String[] si = new String[length];
                String[] ss = new String[length];
                for (int i = 0; i < length; i++) {
                    st[i] = mGame.getSeriesGames()[i].getTitle();
                    si[i] = mGame.getSeriesGames()[i].getBackgroundImage();
                    ss[i] = mGame.getSeriesGames()[i].getSlugName();
                }

                //set up page view, to show the 6 game the platform worked on.
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), si, st, ss);
                viewPager.setAdapter(viewPagerAdapter);
            }else {
                viewPager.setVisibility(View.GONE);
            }

            //pull games data out and provided to the viewPager
            ViewPager viewMorePager = getView().findViewById(R.id.vp_simGames_gd);

            if(mGame.getMoreGames() != null){
                int length = mGame.getMoreGames().length;

                String[] titles = new String[length];
                String[] images = new String[length];
                String[] slugs = new String[length];
                for (int i = 0; i < length ; i++) {
                    titles[i] = mGame.getMoreGames()[i].getTitle();
                    images[i] = mGame.getMoreGames()[i].getBackgroundImage();
                    slugs[i] = mGame.getMoreGames()[i].getSlugName();
                }

                //set up page view, to show the 6 game the platform worked on.
                ViewPagerAdapter viewMorePageAdapter = new ViewPagerAdapter(getContext(), images, titles, slugs);
                viewMorePager.setAdapter(viewMorePageAdapter);
            }
            else {
                viewMorePager.setVisibility(View.GONE);
            }

        }
    }
}
