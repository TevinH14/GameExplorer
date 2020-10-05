package com.example.gameexplorer.fragment.creatorFragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.gameexplorer.model.Creator;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class CreatorDetailFragment extends Fragment implements View.OnClickListener {
    private static Creator mCreators;

    public static CreatorDetailFragment newInstance(Creator _creators) {
        mCreators = _creators;
        return new CreatorDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_creator_detail,container,false);
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
        if(getView() != null && getContext() != null) {

            //assign text view and set creator name
            TextView tv_title = getView().findViewById(R.id.tv_name_cd);
            tv_title.setText(mCreators.getName());

            //assign imageView and set creator image or placeholder
            ImageView iv_image = getView().findViewById(R.id.iv_creatorDetail);
            int height = iv_image.getHeight();
            if(!mCreators.getCreatorImage().matches("null") &&
                    !mCreators.getCreatorImage().matches("") &&
                    mCreators.getCreatorImage() != null) {
                Picasso.get()
                        .load(mCreators.getCreatorImage())
                        .resize(124, height)
                        .into(iv_image);
            }else {
                iv_image.setImageResource(R.drawable.person_placeholder);
            }

            //set Constraint layout background image
            final ConstraintLayout cl_constraint = getView().findViewById(R.id.cl_backgroundImage_cd);
            height = cl_constraint.getHeight();
            Picasso.get()
                    .load(mCreators.getImageUrl())
                    .resize(269,height)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            cl_constraint
                                    .setBackground(new
                                            BitmapDrawable(getContext().getResources(),bitmap));
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

            //set creator name to help users to know what they are looking at.
            TextView tv_moreTitle = getView().findViewById(R.id.tv_moreGame_cd);
            String moreTitle = "Games made by " + mCreators.getName();
            tv_moreTitle.setText(moreTitle);

            TextView tv_viewMore = getView().findViewById(R.id.tv_vm_cd);
            tv_viewMore.setOnClickListener(this);

            //assign the button and add on click listener to hide or show description
            Button btn_description = getView().findViewById(R.id.btn_description_cd);
            btn_description.setOnClickListener(this);

            //pull games data out and provided to the viewPager
            String[] titles = new String[mCreators.getGames().length];
            String[] images = new String[mCreators.getGames().length];
            String[] slugs = new String[mCreators.getGames().length];
            for (int i = 0; i < 6; i++) {
                titles[i] = mCreators.getGames()[i].getTitle();
                images[i] = mCreators.getGames()[i].getBackgroundImage();
                slugs[i] = mCreators.getGames()[i].getSlugName();
            }

            //set up page view, to show the 6 game the creator worked on.
            ViewPager viewPager = getView().findViewById(R.id.vp_games_cd);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext(), images, titles,slugs);
            viewPager.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_description_cd){
            if(getView() != null) {
                View gdView = getView();

                CardView cv = gdView.findViewById(R.id.cv_description_cd);
                ConstraintLayout cl = gdView.findViewById(R.id.cl_textBox_cd);
                if (cl.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    cl.setVisibility(View.GONE);
                } else {
                    TextView tv_description = gdView.findViewById(R.id.tv_description_cd);
                    TransitionManager.beginDelayedTransition(cv, new AutoTransition());
                    tv_description.setText(mCreators.getDescription());
                    cl.setVisibility(View.VISIBLE);
                }
            }
        }
        else if(v.getId() == R.id.tv_vm_cd && getActivity() != null){
            String idString = String.valueOf(mCreators.getId());
            ((CreatorDetailActivity)getActivity())
                    .replaceFragments(NetworkUtils.getGameUrl()
                    +"?creators="
                    +idString);
        }
    }
}
