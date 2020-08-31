package com.example.gameexplorer.networkHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;

public class NetworkTask extends AsyncTask<String, Void, Bitmap> {
    final private OnFinished mOnFinishedInterface;

    public NetworkTask(OnFinished mOnFinishedInterface) {
        this.mOnFinishedInterface = mOnFinishedInterface;
    }
    public interface OnFinished{
        void OnPost(Bitmap image);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        mOnFinishedInterface.OnPost(result);
    }
}