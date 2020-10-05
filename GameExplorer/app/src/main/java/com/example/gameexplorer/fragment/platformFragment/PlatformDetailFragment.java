package com.example.gameexplorer.fragment.platformFragment;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
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
import androidx.viewpager.widget.ViewPager;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.PlatformDetailActivity;
import com.example.gameexplorer.adapter.ViewPagerAdapter;
import com.example.gameexplorer.model.GamePlatform;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.squareup.picasso.Picasso;

public class PlatformDetailFragment extends Fragment implements View.OnClickListener {
    private static GamePlatform mPlatform;

    public static PlatformDetailFragment newInstance(GamePlatform _platform) {
        mPlatform =_platform;
        return new PlatformDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_platform_detail,container,false);
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
        if(getView() != null) {

            //assign text view and set platform name
            TextView tv_title = getView().findViewById(R.id.tv_title_platformDetail);
            tv_title.setText(mPlatform.getName());

            //assign imageView and set platform image or placeholder
            ImageView iv_image = getView().findViewById(R.id.iv_platformImage);
            Picasso
                    .get()
                    .load(mPlatform.getImageUrl())
                    .resize(300,iv_image.getHeight())
                    .into(iv_image);

            //set creator name to help users to know what they are looking at.
            TextView tv_more = getView().findViewById(R.id.tv_moreGame_pd);
            String moreTitle = "More " + mPlatform.getName() + " Games";
            tv_more.setText(moreTitle);

            TextView tv_viewMore = getView().findViewById(R.id.tv_vm_pd);
            tv_viewMore.setOnClickListener(this);

            //assign the button and add on click listener to hide or show description
            Button btn_description = getView().findViewById(R.id.btn_description_pd);
            btn_description.setOnClickListener(this);

            //pull games data out and provided to the viewPager
            String[] titles = new String[mPlatform.getGames().length];
            String[] images = new String[mPlatform.getGames().length];
            String[] slugs = new String[mPlatform.getGames().length];
            for (int i = 0; i < 6; i++) {
                titles[i] = mPlatform.getGames()[i].getTitle();
                images[i] = mPlatform.getGames()[i].getBackgroundImage();
                slugs[i] = mPlatform.getGames()[i].getSlugName();
            }

            //set up page view, to show the 6 game the platform worked on.
            ViewPager viewPager = getView().findViewById(R.id.vp_games_pd);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), images, titles,slugs);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        //expand or collapse description cardView
        if(v.getId() == R.id.btn_description_pd && getView() != null){
            if(getView() != null) {
                View gdView = getView();
                CardView cv = gdView.findViewById(R.id.cv_description_pd);
                ConstraintLayout cl = gdView.findViewById(R.id.cl_textBox_pd);
                if (cl.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    cl.setVisibility(View.GONE);
                } else {
                    TextView tv_description = gdView.findViewById(R.id.tv_description_pd);
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    tv_description.setText(mPlatform.getDescription());
                    cl.setVisibility(View.VISIBLE);
                }
            }
        }
        //send user to view more publisher games
        else if(v.getId() == R.id.tv_vm_pd && getActivity() != null){
            String idString = String.valueOf(mPlatform.getId());
            ((PlatformDetailActivity)getActivity())
                    .replaceFragments(NetworkUtils.getGameUrl()
                            +"?platforms="
                            +idString);
        }
    }
}
