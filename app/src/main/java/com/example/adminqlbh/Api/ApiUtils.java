package com.example.adminqlbh.Api;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private ApiUtils(){
    }
    // Cung cấp interface ApiService bằng một phương thức tĩnh getAPIService()
    public static ApiService getApiService(){
        return RetrofitClient.getClient().create(ApiService.class);
    }

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(float secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, (long) (secs * 1000)); // afterDelay will be executed after (secs*1000) milliseconds.
    }
    // check empty editText
    public static boolean isEmpty(EditText editText){
        CharSequence chrs = editText.getText().toString();
        return TextUtils.isEmpty(chrs);
    }

    // Encode ImageView to String
    public static String EncodeImageViewToString(ImageView iv){
        String strImg = "";
        Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //compress bitmap
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArrImg = outputStream.toByteArray();
        // encode byte to base64 string
        strImg = Base64.encodeToString(byteArrImg, Base64.DEFAULT);
        return strImg;
    }

    // Decode string to ImageView
    public static ImageView DecodeStringToImageView(ImageView imageView,String strImage){
        byte[] bytes = Base64.decode(strImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        // Set bitmap on imageView
        imageView.setImageBitmap(bitmap);
        return imageView;
    }
}



