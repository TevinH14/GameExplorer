package com.example.gameexplorer.fragment.developerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.CreatorDetailActivity;
import com.example.gameexplorer.activity.DeveloperDetailActivity;
import com.example.gameexplorer.adapter.ViewPagerAdapter;
import com.example.gameexplorer.model.Developer;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DeveloperDetailFragment extends Fragment implements
View.OnClickListener{
    private static Developer mDeveloper;

    public static DeveloperDetailFragment newInstance(Developer _developer) {
        mDeveloper = _developer;
        return new DeveloperDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_developer_detail,container,false);
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
            TextView tv_title = getView().findViewById(R.id.tv_title_developerDetail);
            tv_title.setText(mDeveloper.getName());

            ImageView iv_image = getView().findViewById(R.id.iv_developerImage);
            Picasso
                    .get()
                    .load(mDeveloper.getImageUrl())
                    .resize(iv_image.getWidth(),200)
                    .into(iv_image);

            TextView tv_more = getView().findViewById(R.id.tv_moreGame_devD);
            String moreTitle = "More " + mDeveloper.getName() + " Games";
            tv_more.setText(moreTitle);

            TextView tv_viewMore = getView().findViewById(R.id.tv_vm_pubD);
            tv_viewMore.setOnClickListener(this);

            int length = mDeveloper.getGames().length;
            String[] titles = new String[length];
            String[] images = new String[length];
            String[] slugs = new String[length];
            for (int i = 0; i < length; i++) {
                titles[i] = mDeveloper.getGames()[i].getTitle();
                images[i] = mDeveloper.getGames()[i].getBackgroundImage();
                slugs[i] = mDeveloper.getGames()[i].getSlugName();
            }

            ViewPager viewPager = getView().findViewById(R.id.vp_games_devD);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), images, titles,slugs);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_vm_pubD && getActivity() != null){
            String idString = String.valueOf(mDeveloper.getId());
            ((DeveloperDetailActivity)getActivity())
                    .replaceFragments(NetworkUtils.getGameUrl()
                            +"?developers="
                            +idString);
        }
    }
}
