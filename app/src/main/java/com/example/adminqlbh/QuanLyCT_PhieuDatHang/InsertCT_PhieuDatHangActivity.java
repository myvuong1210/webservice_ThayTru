
package com.example.adminqlbh.QuanLyCT_PhieuDatHang;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaAdapter;
import com.example.adminqlbh.QuanLyPhieuDatHang.CT_PhieuDatHangAdapter;
import com.example.adminqlbh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity.curentPositionCTPDH;
import static com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity.tempListCTPDH;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.TAG;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.tempListHH;

public class InsertCT_PhieuDatHangActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputThanhTien, txtInputSoluong;
    private Button btnAddCTPDH;
    private boolean isInsertSuccess = false;
    private ApiService apiService;

    Spinner spinnerIDPDH, spinnerIDHH;
    ArrayList<HangHoa> listhangHoa = new ArrayList<>();
    ArrayList<PhieuDatHang> listPhieuDatHang = new ArrayList<>();


    ArrayAdapter<HangHoa> arrayAdapterHangHoa;
    ArrayAdapter<PhieuDatHang> arrayAdapterPhieuDatHang;
    private String TAG;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_chitietdathang);
        loadDataHangHoa();
        loadDataPDH();
        setControl();
        ActionBar();
        setEvent();
        }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void loadDataHangHoa() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<HangHoa>> call = apiService.getListProduct();
        call.enqueue(new Callback<List<HangHoa>>() {
            @Override
            public void onResponse(Call<List<HangHoa>> call, Response<List<HangHoa>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<HangHoa> listTemp = new ArrayList<>();
                        listTemp = response.body();
                        listhangHoa.clear();
                        for (HangHoa temp : listTemp) {
                            listhangHoa.add(temp);
                        }
                        setControl();
                        setEvent();

                    } catch (Exception e) {
                        Log.e("Không thể load được dữ liệu hàng hóa", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HangHoa>> call, Throwable t) {
                Log.e("Không thể load được dữ liệu hàng hóa", t.getMessage());
            }
        });
    }


    public void loadDataPDH() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<PhieuDatHang>> call = apiService.getPhieuDatHang();
        call.enqueue(new Callback<List<PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<PhieuDatHang>> call, Response<List<PhieuDatHang>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<PhieuDatHang> listTemp = new ArrayList<>();
                        listTemp = response.body();
                        listPhieuDatHang.clear();
                        for (PhieuDatHang temp : listTemp)
                            listPhieuDatHang.add(temp);
                        setControl();
                        setEvent();
                    } catch (Exception e) {
                        Log.e("Không thể load được dữ liệu hàng hóa", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhieuDatHang>> call, Throwable t) {
                Log.e("Không thể load được dữ liệu hàng hóa", t.getMessage());

            }
        });
    }


    private void setControl() {
        txtInputId = findViewById(R.id.txtInputIdCTPDH);
        txtInputThanhTien = findViewById(R.id.txtInputThanhTienCTPDH);
        txtInputSoluong = findViewById(R.id.txtInputSoluongCTPDH);
        spinnerIDHH = (Spinner) findViewById(R.id.spinnerIDHH_DH);
        spinnerIDPDH = (Spinner) findViewById(R.id.spinnerIDPDH);
        arrayAdapterHangHoa = new ArrayAdapter<HangHoa>(InsertCT_PhieuDatHangActivity.this, android.R.layout.simple_spinner_item, listhangHoa);
        arrayAdapterPhieuDatHang = new ArrayAdapter<PhieuDatHang>(InsertCT_PhieuDatHangActivity.this, android.R.layout.simple_spinner_dropdown_item, listPhieuDatHang);
        arrayAdapterHangHoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPhieuDatHang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIDPDH.setAdapter(arrayAdapterPhieuDatHang);
        spinnerIDHH.setAdapter(arrayAdapterHangHoa);
        btnAddCTPDH = findViewById(R.id.luuCTPDH);
        toolbar=findViewById(R.id.toolbar);
        btnAddCTPDH.setText("THÊM");
    }

    public Boolean checkId(String id) {
        String emailPattern = "[a-zA-Z0-9_-]{1,8}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(id);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }



    //====================Validate======================
    public boolean isValidInput(){
        if(checkId(txtInputId.getText().toString())==false){
            txtInputId.setError("ID không hợp lệ, kiểm tra lại !");
            return false;
        }

        if (ApiUtils.isEmpty(txtInputSoluong)) {
            txtInputSoluong.setError("Khối lượng hàng hóa không được để trống ");
            return false;
        }
        if (ApiUtils.isEmpty(txtInputThanhTien)) {
            txtInputThanhTien.setError("Thành tiền không được để trống !");
            return false;
        }
        return true;
    }


    private CT_PhieuDatHang getCT_PhieuDatHang() {
        CT_PhieuDatHang CTPDH = new CT_PhieuDatHang();
        CTPDH.setId(txtInputId.getText().toString());
        CTPDH.setIdHH(String.valueOf(spinnerIDHH.getSelectedItem()));
        CTPDH.setIdPhieuDatHang(String.valueOf(spinnerIDPDH.getSelectedItem()));
        CTPDH.setSoLuong(Integer.valueOf(txtInputSoluong.getText().toString().trim()));
        CTPDH.setThanhTien(Integer.valueOf(txtInputThanhTien.getText().toString().trim()));
        return CTPDH;
    }

    private void insertCT_PhieuDatHang() {
        apiService = ApiUtils.getApiService();
        apiService.postCT_PhieuDatHang(getCT_PhieuDatHang()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListCTPDH.add(getCT_PhieuDatHang());
                    finish();

                }
                if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "ID đã tồn tại !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: ID đã tồn tại !" + response.body());
                    isInsertSuccess = false;

                }
                if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "Lỗi server !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: Lỗi server !" + response.body());
                    isInsertSuccess = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Thêm chi tiết phiếu nhập hàng ", "onFailure: " + t.getMessage());
                isInsertSuccess = false;
            }
        });
    }

    private void setEvent() {
        btnAddCTPDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    insertCT_PhieuDatHang();
                    // Delay to to finish insert hang hoa
//                    ApiUtils.delay(4, new ApiUtils.DelayCallback() {
//                        @Override
//                        public void afterDelay() {
//                            if(isInsertSuccess){
//
//                            }
//                        }
//                    });
                }
            }
        });

    }
}



