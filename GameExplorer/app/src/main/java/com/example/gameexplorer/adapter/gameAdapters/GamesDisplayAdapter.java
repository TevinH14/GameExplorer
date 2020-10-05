package com.example.gameexplorer.adapter.gameAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gameexplorer.R;
import com.example.gameexplorer.model.Games;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GamesDisplayAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Games> mGamesList;

    public GamesDisplayAdapter(Context _context, ArrayList<Games> _gamesList) {
        mContext =_context;
        mGamesList = _gamesList;

    }



    @Override
    public int getCount() {
        if (mGamesList != null){
            return mGamesList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if( mGamesList!= null && mGamesList.size() > position){
            return mGamesList.get(position);
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
        if (mGamesList != null) {
            Games obj = mGamesList.get(position);
            vh.textHolder.setText(obj.getTitle());

            Picasso
                    .get()
                    .load(obj.getBackgroundImage())
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
