package com.thaneshdavuluri.dosakaya;

/**
 * Created by thanesh.davuluri on 6/6/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageTask extends AsyncTask<String, String, Bitmap> {

    public LoadImageTask(Listener listener) {

        mListener = listener;
    }

    public interface Listener{

        void onImageLoaded(Bitmap bitmap);
        void onError();
    }

    private Listener mListener;
    @Override
    protected Bitmap doInBackground(String... args) {

        try {
            Log.d("Content",args[0]);
            return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) {

            mListener.onImageLoaded(bitmap);

        } else {

            mListener.onError();
        }
    }
    private byte[] downloadImage(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            InputStream is = httpCon.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead, totalBytesRead = 0;
            byte[] data = new byte[2048];
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
