package com.example.gameexplorer.adapter.gameAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameexplorer.R;
import com.squareup.picasso.Picasso;

public class GameDetailAdapter extends RecyclerView.Adapter<GameDetailAdapter.ViewHolder> {
    private String[] gameUrlList;
    private boolean mIsVideo;
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position, boolean isVideo);
    }

    public GameDetailAdapter(String[] gameUrlList, boolean mIsVideo) {
        this.gameUrlList = gameUrlList;
        this.mIsVideo = mIsVideo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_detail_gallery_item, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso
                .get()
                .load(gameUrlList[position])
                .resize(300,100)
                .into(holder.gameImage);

        holder.gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position,mIsVideo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameUrlList.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gameImage;
        ViewHolder(View view) {
            super(view);
             this.gameImage = view.findViewById(R.id.iv_rv_image);
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
