package com.example.adminqlbh.Api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AsyncTaskLoadProduct extends AsyncTask<String, Void, Boolean> {
    ImageView imgSP;
    TextView tvTenSP;
    ApiService apiService;
    HangHoa hh;

    public AsyncTaskLoadProduct(ImageView imgSP, TextView tvTenSP) {
        this.imgSP = imgSP;
        this.tvTenSP = tvTenSP;
    }

    @Override
    protected Boolean doInBackground(String... id) {
        String idHH = id[0];
        apiService = ApiUtils.getApiService();
        apiService.getProductById(idHH).enqueue(new Callback<HangHoa>() {
            @Override
            public void onResponse(Call<HangHoa> call, Response<HangHoa> response) {
                if(response.isSuccessful()){
                    hh = new HangHoa();
                    hh = response.body();
                    Picasso.get().load(RetrofitClient.baseURLImage + hh.getAnh())
                            .placeholder(R.drawable.no_image_icon)
                            .into(imgSP);
                    tvTenSP.setText(hh.getTen());
                }else {
                    Errors.initListError();
                    Log.d("Error response ", "onResponse: " + Errors.listErrors.get(response.code()));
                }
            }

            @Override
            public void onFailure(Call<HangHoa> call, Throwable t) {

            }
        });
        return true;
    }

    @Override
    protected void onPostExecute(Boolean loadSuccess) {
    }
}
