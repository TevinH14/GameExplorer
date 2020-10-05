package com.example.gameexplorer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameexplorer.R;
import com.example.gameexplorer.model.Developer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeveloperAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Developer> mDeveloper;

    public DeveloperAdapter(Context mContext, ArrayList<Developer> mDeveloper) {
        this.mContext = mContext;
        this.mDeveloper = mDeveloper;
    }

    @Override
    public int getCount() {
        return mDeveloper.size();
    }

    @Override
    public Object getItem(int position) {
        if( mDeveloper!= null && mDeveloper.size() > position){
            return mDeveloper.get(position);
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
        if (mDeveloper != null) {
            Developer obj = mDeveloper.get(position);
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
