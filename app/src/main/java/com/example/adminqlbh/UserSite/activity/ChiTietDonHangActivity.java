package com.example.adminqlbh.UserSite.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.QuanLyPhieuDatHang.PhieuDatHang_Adapter;
import com.example.adminqlbh.R;
import com.example.adminqlbh.UserSite.adapter.ChiTietDonHangAdapter;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietDonHangActivity extends AppCompatActivity {

    private Toolbar toolbarCT_Donhang;
    private ListView lvDonhang;
    private TextView tvTongtien;
    private Button btnTieptucmuahang;
    private List<CT_PhieuDatHang> ct_phieuDatHangList;
    private ApiService apiService;
    private ChiTietDonHangAdapter ctAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_activity_chitietdonhang);
        migrateComponent();
        actionBar();
        getCT_DonhangByID();
        btnTieptucmuaEvent();
    }

    private void btnTieptucmuaEvent() {
        btnTieptucmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietDonHangActivity.this, UserHomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getCT_DonhangByID() {
        String idDonhang = getIntent().getStringExtra("ID DON DAT HANG");
        Log.d("danh sách đơn hàng ", "onResponse: " + idDonhang);
        apiService = ApiUtils.getApiService();
        apiService.getCT(idDonhang).enqueue(new Callback<List<CT_PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<CT_PhieuDatHang>> call, Response<List<CT_PhieuDatHang>> response) {
                ct_phieuDatHangList = response.body();

//                for(CT_PhieuDatHang i : ct_phieuDatHangList){
//                    Log.d("danh sách đơn hàng ", "onResponse: " + i.getSoLuong());
//                }
                setAdapterCT_Donhang(ct_phieuDatHangList);
                tongTienDonHang(ct_phieuDatHangList);
            }

            @Override
            public void onFailure(Call<List<CT_PhieuDatHang>> call, Throwable t) {

            }
        });


    }

    private void tongTienDonHang(List<CT_PhieuDatHang> ctList) {
        int tongtien = 0;
        for(int i = 0; i < ctList.size(); i++){
            tongtien += ctList.get(i).getThanhTien();
        }
        // Format String to vietnamese currency
        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTongtien.setText(formatGia.format(tongtien));
    }

    private void setAdapterCT_Donhang(List<CT_PhieuDatHang> ct_phieuDatHangList) {
        if(ctAdapter == null){
            // set adapter san pham
            ctAdapter = new ChiTietDonHangAdapter(ct_phieuDatHangList, this);
            lvDonhang.setAdapter(ctAdapter);
        }
        else {
            ctAdapter.notifyDataSetChanged();
        }
    }

    private void migrateComponent() {
        toolbarCT_Donhang = findViewById(R.id.toolbarCT_Donhang);
        lvDonhang = findViewById(R.id.listViewCT_Donhang);
        btnTieptucmuahang = findViewById(R.id.btnTiepTucMuaSam);
        tvTongtien = findViewById(R.id.tvDonhangTongTien);
    }

    // sự kiện actionBar back click
    private void actionBar(){
        setSupportActionBar(toolbarCT_Donhang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCT_Donhang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}