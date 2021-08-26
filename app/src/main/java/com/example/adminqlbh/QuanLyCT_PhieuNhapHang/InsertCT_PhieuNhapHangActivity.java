
package com.example.adminqlbh.QuanLyCT_PhieuNhapHang;

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
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.Models.PhieuNhapHang;
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

import static com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity.curentPositionCTPNH;
import static com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity.tempListCTPNH;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.TAG;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.tempListHH;

public class InsertCT_PhieuNhapHangActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputThanhTien, txtInputSoluong;
    private Button btnAddCTPNH;
    private boolean isInsertSuccess = false;
    private ApiService apiService;

    Spinner spinnerIDPNH, spinnerIDHH;
    ArrayList<HangHoa> listhangHoa = new ArrayList<>();
    ArrayList<PhieuNhapHang> listPhieuNhapHang = new ArrayList<>();


    ArrayAdapter<HangHoa> arrayAdapterHangHoa;
    ArrayAdapter<PhieuNhapHang> arrayAdapterPhieuNhapHang;
    private String TAG;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_chitietnhaphang);
        loadDataHangHoa();
        loadDataPNH();
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


    public void loadDataPNH() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<PhieuNhapHang>> call = apiService.getListPhieuNhapHang();
        call.enqueue(new Callback<List<PhieuNhapHang>>() {
            @Override
            public void onResponse(Call<List<PhieuNhapHang>> call, Response<List<PhieuNhapHang>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<PhieuNhapHang> listTemp = new ArrayList<>();
                        listTemp = response.body();
                        listPhieuNhapHang.clear();
                        for (PhieuNhapHang temp : listTemp)
                            listPhieuNhapHang.add(temp);
                        setControl();
                        setEvent();
                    } catch (Exception e) {
                        Log.e("Không thể load được dữ liệu hàng hóa", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhieuNhapHang>> call, Throwable t) {
                Log.e("Không thể load được dữ liệu hàng hóa", t.getMessage());

            }
        });
    }


    private void setControl() {
        txtInputId = findViewById(R.id.txtInputIdCTPNH);
        txtInputThanhTien = findViewById(R.id.txtInputThanhTienCTPNH);
        txtInputSoluong = findViewById(R.id.txtInputSoluongCTPNH);
        spinnerIDHH = (Spinner) findViewById(R.id.spinnerIDHH);
        spinnerIDPNH = (Spinner) findViewById(R.id.spinnerIDPNH);
        arrayAdapterHangHoa = new ArrayAdapter<HangHoa>(InsertCT_PhieuNhapHangActivity.this, android.R.layout.simple_spinner_item, listhangHoa);
        arrayAdapterPhieuNhapHang = new ArrayAdapter<PhieuNhapHang>(InsertCT_PhieuNhapHangActivity.this, android.R.layout.simple_spinner_dropdown_item, listPhieuNhapHang);
        arrayAdapterHangHoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPhieuNhapHang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIDPNH.setAdapter(arrayAdapterPhieuNhapHang);
        spinnerIDHH.setAdapter(arrayAdapterHangHoa);
        btnAddCTPNH = findViewById(R.id.luuCTPNH);
        toolbar = findViewById(R.id.toolbar);
        btnAddCTPNH.setText("THÊM");
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
    public boolean isValidInput() {
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


    private CT_PhieuNhapHang getCT_PhieuNhapHang() {
        CT_PhieuNhapHang CTPNH = new CT_PhieuNhapHang();
        CTPNH.setId(txtInputId.getText().toString().toUpperCase());
        CTPNH.setIdhh(String.valueOf(spinnerIDHH.getSelectedItem()));
        CTPNH.setIdPhieuNhapHang(String.valueOf(spinnerIDPNH.getSelectedItem()));
        CTPNH.setSoLuong(Integer.valueOf(txtInputSoluong.getText().toString().trim()));
        CTPNH.setThanhTien(Integer.valueOf(txtInputThanhTien.getText().toString().trim()));
        return CTPNH;
    }

    private void insertCT_PhieuNhapHang() {
        apiService = ApiUtils.getApiService();
        apiService.postCT_PhieuNhapHang(getCT_PhieuNhapHang()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListCTPNH.add(getCT_PhieuNhapHang());
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

    public int getHangHoaPosition(String idHanghoa) {
        for (int i = 0; i < listhangHoa.size(); i++) {
            if (idHanghoa == listhangHoa.get(i).getId())
                return i;
        }
        return -1;
    }

    public int getDonHangPosition(String idDonhang) {
        for (int i = 0; i < listPhieuNhapHang.size(); i++) {
            if (idDonhang == listPhieuNhapHang.get(i).getId())
                return i;
        }
        return -1;
    }

    private void setEvent() {
        btnAddCTPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    insertCT_PhieuNhapHang();
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



