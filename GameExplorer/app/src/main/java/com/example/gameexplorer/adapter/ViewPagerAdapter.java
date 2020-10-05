package com.example.gameexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gameexplorer.R;
import com.example.gameexplorer.activity.GameDetailActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;

    private String[] mImageArray;
    private String[] titles;
    private String[] slugs;

    public ViewPagerAdapter(Context mContext, String[] mImageArray, String[] titles,String[] slugs) {
        this.mContext = mContext;
        this.mImageArray = mImageArray;
        this.titles = titles;
        this.slugs =slugs;
    }

    @Override
    public int getCount() {
        return mImageArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.vp_display_layout, null);

        ImageView imageView = view.findViewById(R.id.iv_vpCustom);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameDetailIntent = new Intent(mContext, GameDetailActivity.class);
                gameDetailIntent.putExtra(GameDetailActivity.GAME_DETAIL_EXTRA,slugs[position]);
                mContext.startActivity(gameDetailIntent);

            }
        });
        Picasso
                .get()
                .load(mImageArray[position])
                .resize(imageView.getWidth(),200)
                .into(imageView,new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
        TextView tv_title = view.findViewById(R.id.tv_vpCustom);
        tv_title.setText(titles[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
