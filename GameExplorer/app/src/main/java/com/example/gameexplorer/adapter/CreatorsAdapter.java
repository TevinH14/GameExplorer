package com.example.gameexplorer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gameexplorer.R;
import com.example.gameexplorer.model.Creator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class CreatorsAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Creator> mCreator;
    private ViewHolder mVh;
    public CreatorsAdapter(Context mContext, ArrayList<Creator> mCreator) {
        this.mContext = mContext;
        this.mCreator = mCreator;
    }

    @Override
    public int getCount() {
        return mCreator.size();
    }

    @Override
    public Object getItem(int position) {
        if( mCreator!= null && mCreator.size() > position){
            return mCreator.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.creator_display, parent, false);
            mVh = new ViewHolder(convertView);
            convertView.setTag(mVh);
        }else {
            mVh = (ViewHolder) convertView.getTag();
        }
        if (mCreator != null) {
            Creator obj = mCreator.get(position);
            mVh.tv_name.setText(obj.getName());
            String numCount = String.valueOf(obj.getGameCount());
            mVh.tv_gameCount.setText(numCount);

            int height = mVh.imageHolder.getHeight();
            if(!obj.getCreatorImage().matches("null") &&
                    !obj.getCreatorImage().matches("") &&
                    obj.getCreatorImage() != null) {
                Picasso
                        .get()
                        .load(obj.getCreatorImage())
                        .resize(124, height)
                        .into(mVh.imageHolder, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
            }else {
                mVh.imageHolder.setImageResource(R.drawable.person_placeholder);
            }

            height = mVh.cl_constraintHolder.getHeight();
            Picasso
                    .get()
                    .load(obj.getImageUrl())
                    .resize(269,height)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mVh.cl_constraintHolder
                                    .setBackground(new
                                            BitmapDrawable(mContext.getResources(),bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            return convertView;
        }
        return null;
    }

    static class ViewHolder{
        final ImageView imageHolder;
        final TextView tv_name;
        final TextView tv_gameCount;
        final ConstraintLayout cl_constraintHolder;

        public ViewHolder(View layout) {
            this.imageHolder = layout.findViewById(R.id.iv_creator_cd);
            this.tv_name = layout.findViewById(R.id.tv_name_cd);
            this.tv_gameCount = layout.findViewById(R.id.tv_gameCount_cd);
            this.cl_constraintHolder = layout.findViewById(R.id.cl_backgroundImage_cd);
        }
    }
}
