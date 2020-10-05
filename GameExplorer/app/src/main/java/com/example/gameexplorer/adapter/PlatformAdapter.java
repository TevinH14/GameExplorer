package com.example.gameexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameexplorer.R;
import com.example.gameexplorer.model.GamePlatform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlatformAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<GamePlatform> mPlatforms;

    public PlatformAdapter(Context mContext, ArrayList<GamePlatform> mPlatforms) {
        this.mContext = mContext;
        this.mPlatforms = mPlatforms;
    }

    @Override
    public int getCount() {
        return mPlatforms.size();
    }

    @Override
    public Object getItem(int position) {
        if( mPlatforms!= null && mPlatforms.size() > position){
            return mPlatforms.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.games_display_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (mPlatforms != null) {
            GamePlatform obj = mPlatforms.get(position);
            vh.textHolder.setText(obj.getName());

            Picasso
                    .get()
                    .load(obj.getImageUrl())
                    .resize(375,150)
                    .into(vh.imageHolder,new Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });

            return convertView;
        }
        return null;
    }

    static class ViewHolder{
        final ImageView imageHolder;
        final TextView textHolder;

        public ViewHolder(View layout) {
            this.imageHolder = layout.findViewById(R.id.iv_gdImage);
            this.textHolder = layout.findViewById(R.id.tv_gdTitle);
        }
    }
}
