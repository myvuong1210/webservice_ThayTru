package com.example.adminqlbh.UserSite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.GioHang;
import com.example.adminqlbh.Models.HangHoaFull;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolbarCTSP;
    ImageView imageViewCTSP;
    TextView textViewTenCTSP, textViewGiaCTSP, textViewMotaCTSP, textViewSlTon, textViewThongBao;
    Spinner spinnerSoLuong;
    Button btnThemGioHang;
    private int soluongTon;
    private String idCTSP;
    private BigDecimal giaChiTiet;
    private String tenSP;
    private String imageSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_user_chitietsanpham);
        super.onCreate(savedInstanceState);
        migrateComponent();
        actionToolbarCTSP();
        getChiTietSanPham();
        eventSpinner();
        eventBtnThemGioHang();
    }

    private void eventBtnThemGioHang() {
        btnThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soluongTon > 0){
                    if(UserHomePageActivity.listGioHang.size() > 0){
                        int sl = Integer.parseInt((spinnerSoLuong.getSelectedItem().toString()));
                        boolean isExist = false;
                        for (int i = 0; i < UserHomePageActivity.listGioHang.size(); i++){
                            if(UserHomePageActivity.listGioHang.get(i).getIdSP() == idCTSP){
                                // cập nhật số lượng mua
                                UserHomePageActivity.listGioHang.get(i).setSoluongmua(UserHomePageActivity.listGioHang.get(i).getSoluongmua() +
                                        sl);
                                // khi số lượng mua > số lượng tồn
                                if (UserHomePageActivity.listGioHang.get(i).getSoluongmua() >= soluongTon){
                                    UserHomePageActivity.listGioHang.get(i).setSoluongmua(soluongTon);
                                }
                                // tính số tiền
                                BigDecimal thanhtien = giaChiTiet.multiply(BigDecimal.valueOf(UserHomePageActivity.listGioHang.get(i).getSoluongmua()));
                                UserHomePageActivity.listGioHang.get(i).setGiaSP(thanhtien);
                                isExist = true;
                            }
                        }
                        if(!isExist){
                            int soluongmua = Integer.parseInt((spinnerSoLuong.getSelectedItem().toString()));
                            BigDecimal thanhtien = BigDecimal.ZERO; // = 0.0
                            thanhtien = giaChiTiet.multiply(BigDecimal.valueOf(soluongmua));
                            UserHomePageActivity.listGioHang.add(new GioHang(idCTSP, tenSP, thanhtien, imageSP,soluongmua, soluongTon));
                        }
                    }
                    else {
                        int soluongmua = Integer.parseInt((spinnerSoLuong.getSelectedItem().toString()));
                        BigDecimal thanhtien = BigDecimal.ZERO; // = 0.0
                        thanhtien = giaChiTiet.multiply(BigDecimal.valueOf(soluongmua));
                        UserHomePageActivity.listGioHang.add(new GioHang(idCTSP, tenSP, thanhtien, imageSP,soluongmua, soluongTon));
                    }

                    Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else Toast.makeText(ChiTietSanPhamActivity.this, "Sản phẩm đã bán hết" +
                        "\nMời bạn chọn sản phẩm khác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eventSpinner() {
        ArrayList<Integer> arrSoLuong;
        if(soluongTon == 0){
            spinnerSoLuong.setVisibility(View.GONE);
            textViewThongBao.setVisibility(View.VISIBLE);
        }
        // Chỉ chọn tối đa 5 sản phẩm
        if(soluongTon > 5){
            arrSoLuong = new ArrayList<>();
            for(int i = 1; i <=5; i++){
                arrSoLuong.add(i);
            }
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,
                    android.R.layout.simple_spinner_dropdown_item, arrSoLuong);
            spinnerSoLuong.setAdapter(arrayAdapter);
            spinnerSoLuong.setSelection(0);
        }
        else {
            arrSoLuong = new ArrayList<>();
            for(int i = 1; i <= soluongTon; i++){
                arrSoLuong.add(i);
            }
            ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,
                    android.R.layout.simple_spinner_dropdown_item, arrSoLuong);
            spinnerSoLuong.setAdapter(arrayAdapter);
            spinnerSoLuong.setSelection(0);
        }
    }

    private void getChiTietSanPham() {
        try {
            HangHoaFull hangHoaFull = (HangHoaFull) getIntent().getSerializableExtra("sanpham");
            Picasso.get().load(RetrofitClient.baseURLImage + hangHoaFull.getAnh())
                    .placeholder(R.drawable.no_image_icon)
                    .into(imageViewCTSP);
            textViewTenCTSP.setText(hangHoaFull.getTen());
            // Format String to vietnamese currency
            Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            textViewGiaCTSP.setText("Giá : " + formatGia.format(hangHoaFull.getGia()));
            textViewSlTon.setText("Hàng có sẵn : " + String.valueOf(hangHoaFull.getSoluongTon()) + " sản phẩm");
            textViewMotaCTSP.setText(hangHoaFull.getMoTa());
            soluongTon = hangHoaFull.getSoluongTon();
            giaChiTiet = hangHoaFull.getGia();
            idCTSP = hangHoaFull.getId();
            tenSP = hangHoaFull.getTen();
            imageSP = hangHoaFull.getAnh();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void actionToolbarCTSP() {
        setSupportActionBar(toolbarCTSP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCTSP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void migrateComponent() {
        toolbarCTSP = findViewById(R.id.toolbarCTSP);
        imageViewCTSP = findViewById(R.id.imageviewChitietsanpham);
        textViewTenCTSP = findViewById(R.id.textviewTenCTSP);
        textViewGiaCTSP= findViewById(R.id.textviewGiaCTSP);
        textViewMotaCTSP = findViewById(R.id.textviewMotaCTSP);
        textViewSlTon = findViewById(R.id.textviewSLTonCTSP);
        textViewThongBao = findViewById(R.id.textviewThongBaoHetHang);
        spinnerSoLuong = findViewById(R.id.spinnerSoLuongSP);
        btnThemGioHang = findViewById(R.id.buttonThemGioHang);
    }

    // icon cart click event
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_giohang, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGiohang:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
