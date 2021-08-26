package com.example.adminqlbh.Api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskDownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    // Constructor
    public AsyncTaskDownloadImage(ImageView imgView) {
        this.imageView = imgView;
    }

    // download image from url in background UI
    @Override
    protected Bitmap doInBackground(String... url) {
        String firebaseURL = url[0];
        Bitmap bitmap = null;
        try{
            InputStream in = new URL(firebaseURL).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        }catch (FileNotFoundException | MalformedURLException e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // return result
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
