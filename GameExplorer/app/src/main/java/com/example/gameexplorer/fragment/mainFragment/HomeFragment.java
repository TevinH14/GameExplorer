package com.example.gameexplorer.fragment.mainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.HomeActivity;
import com.example.gameexplorer.adapter.ViewPagerAdapter;
import com.example.gameexplorer.model.Games;
import com.example.gameexplorer.model.HomeGamesCollection;
import com.example.gameexplorer.networkHelper.gameTasks.HomeFragmentTask;
import com.example.gameexplorer.networkHelper.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements HomeFragmentTask.OnDetailFinished, View.OnClickListener{
    private Integer [] mViewPagerId = {
                    R.id.vp_recentlyReleased,
                    R.id.vp_upcomingReleases,
                    R.id.vp_popularReleased,
                    R.id.vp_topRated};

    private HomeFragmentTask mHomeTask;
    private ProgressBar mProgressbar;
    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null){
            mProgressbar = getView().findViewById(R.id.pb_homeLoad);
            TextView tv_recentReleased = getView().findViewById(R.id.tv_vm_recentReleased);
            TextView tv_upcomingRelease = getView().findViewById(R.id.tv_vm_upcomingReleases);
            TextView tv_mostPopular = getView().findViewById(R.id.tv_vm_mostPopular);
            TextView tv_topRated = getView().findViewById(R.id.tv_vm_topRated);
            tv_recentReleased.setOnClickListener(this);
            tv_upcomingRelease.setOnClickListener(this);
            tv_mostPopular.setOnClickListener(this);
            tv_topRated.setOnClickListener(this);

            mHomeTask = new HomeFragmentTask(this);
            mHomeTask.execute();
        }
    }


    @Override
    public void onGamePre() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGamePost(HashMap<Integer, ArrayList<HomeGamesCollection>> _retValues) {
        if(getView() != null) {
            String[] mUrls = mHomeTask.getCategoryStringArray();
            TextView tv_topRatedTitle = getView().findViewById(R.id.tv_topRated_home);
            String topRatedString = "Top Rated game Of " + NetworkUtils.getmRandomYear();
            tv_topRatedTitle.setText(topRatedString);

            for (HashMap.Entry<Integer, ArrayList<HomeGamesCollection>> item : _retValues.entrySet()) {
                ViewPager viewPager = getView().findViewById(mViewPagerId[item.getKey()]);

                ArrayList<HomeGamesCollection> collection = item.getValue();

                for (int i = 0; i < collection.size(); i++) {
                    String[] titles = new String[5];
                    String[] images = new String[5];
                    String[] slugs = new String[5];
                    Games[] games = new Games[5];

                    HomeGamesCollection hgc = collection.get(i);
                    ArrayList<String> t = hgc.getGameTitles();
                    ArrayList<String> giu = hgc.getGameImageUrl();
                    ArrayList<String> slugList = hgc.getGameSlug();

                    for (int j = 0; j < 5; j++) {
                        titles[j] = t.get(j);
                        images[j] = giu.get(j);
                        slugs[j] = slugList.get(j);
                    }
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), images, titles,slugs);
                    viewPager.setAdapter(viewPagerAdapter);
                }
            }
            mProgressbar.setVisibility(View.GONE);

        }
    }


    @Override
    public void onClick(View v) {
        String url = null;
        if(v.getId() == R.id.tv_vm_recentReleased){
            url = NetworkUtils.getRecentlyUrl();
        }else if(v.getId() == R.id.tv_vm_upcomingReleases){
            url =  NetworkUtils.getUpcomingUrl();
        } else if(v.getId() == R.id.tv_vm_mostPopular){
            url =  NetworkUtils.getPopularGames();
        }else if(v.getId() == R.id.tv_vm_topRated){
            url =  HomeFragmentTask.getTopYear();
        }
        if(url != null && getActivity() != null){
            ((HomeActivity) getActivity()).replaceFragments(null,url);
        }
    }
}
