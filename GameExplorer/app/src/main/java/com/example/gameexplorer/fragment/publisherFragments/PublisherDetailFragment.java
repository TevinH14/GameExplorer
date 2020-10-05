package com.example.gameexplorer.fragment.publisherFragments;

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
import com.example.gameexplorer.activity.CreatorDetailActivity;
import com.example.gameexplorer.adapter.ViewPagerAdapter;
import com.example.gameexplorer.model.Publisher;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.squareup.picasso.Picasso;

public class PublisherDetailFragment extends Fragment
        implements View.OnClickListener {
    private  static Publisher mPublisher;
    public static PublisherDetailFragment newInstance(Publisher publisher) {
        mPublisher = publisher;
        return new PublisherDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_publisher_detail,container,false);
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
            TextView tv_title = getView().findViewById(R.id.tv_title_publisherDetail);
            tv_title.setText(mPublisher.getName());

            ImageView iv_image = getView().findViewById(R.id.iv_publisherImage);
            Picasso
                    .get()
                    .load(mPublisher.getImageUrl())
                    .resize(iv_image.getWidth(),200)
                    .into(iv_image);

            TextView tv_more = getView().findViewById(R.id.tv_vm_pubD);
            String moreTitle = "More " + mPublisher.getName() + " Games";
            tv_more.setText(moreTitle);
            tv_more.setOnClickListener(this);

            Button btn_description = getView().findViewById(R.id.btn_description_pubD);
            btn_description.setOnClickListener(this);
            int length = mPublisher.getGames().length;
            String[] titles = new String[length];
            String[] images = new String[length];
            String[] slugs = new String[length];
            for (int i = 0; i < length; i++) {
                titles[i] = mPublisher.getGames()[i].getTitle();
                images[i] = mPublisher.getGames()[i].getBackgroundImage();
                slugs[i] = mPublisher.getGames()[i].getSlugName();
            }

            ViewPager viewPager = getView().findViewById(R.id.vp_games_pubD);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), images, titles,slugs);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_description_pubD && getView() != null){
            if(getView() != null) {
                View gdView = getView();

                CardView cv = gdView.findViewById(R.id.cv_description_pubD);
                ConstraintLayout cl = gdView.findViewById(R.id.cl_textBox_pubD);
                if (cl.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    cl.setVisibility(View.GONE);
                } else {
                    TextView tv_description = gdView.findViewById(R.id.tv_description_pubD);
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    tv_description.setText(mPublisher.getDescription());
                    cl.setVisibility(View.VISIBLE);
                }
            }
            else if(v.getId() == R.id.tv_vm_cd && getActivity() != null){
                String idString = String.valueOf(mPublisher.getId());
                ((CreatorDetailActivity)getActivity())
                        .replaceFragments(NetworkUtils.getGameUrl()
                                +"?publishers="
                                +idString);
            }
        }
    }
}
