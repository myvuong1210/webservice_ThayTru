package com.example.adminqlbh.QuanLyPhieuNhapHang;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.Models.PhieuNhapHang;
import com.example.adminqlbh.R;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.curentPositionKH;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.tempListKH;
import static com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity.curentPositionPNH;
import static com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity.tempListPNH;

public class UpdatePhieuNhapHangActivity extends AppCompatActivity {

    private EditText txtInputIdPNH, txtInputTongTien;
    private Button btnAddPNH;
    private ApiService apiService;

    ArrayAdapter<NhanVien> arrayAdapterNhanVien;

    Spinner spinnerIDNV;
    ArrayList<NhanVien> listNhanVien = new ArrayList<>();
    private String TAG;
    private boolean isInsertSuccess;
    private String id;
    private Toolbar toolbar;
    private TextView themPNH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_phieunhaphang);
        loadDataNhanVien();
        getPNHFromId();
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

    public void loadDataNhanVien() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<NhanVien>> call = apiService.getListNhanVien();
        call.enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful()) {
                    try {
                        List<NhanVien> listTemp = new ArrayList<>();
                        listTemp = response.body();
                        listNhanVien.clear();
                        for (NhanVien temp : listTemp) {
                            listNhanVien.add(temp);
                        }
                        setControl();
                        setEvent();

                    } catch (Exception e) {
                        Log.e("Không thể load được dữ liệu", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Log.e("Không thể load được dữ liệu", t.getMessage());
            }
        });
    }


    private void setControl() {
        txtInputIdPNH = findViewById(R.id.txtInputId_PNH);
        txtInputIdPNH.setFocusable(false);
        txtInputIdPNH.setEnabled(false);
        txtInputIdPNH.setCursorVisible(false);
        txtInputTongTien = findViewById(R.id.txtInputTongTienPNH);
        spinnerIDNV= (Spinner) findViewById(R.id.spinnerIDNV);
        arrayAdapterNhanVien = new ArrayAdapter<NhanVien>(UpdatePhieuNhapHangActivity.this, android.R.layout.simple_spinner_item, listNhanVien);
        spinnerIDNV.setAdapter(arrayAdapterNhanVien);
        toolbar=findViewById(R.id.toolbar);
        btnAddPNH = findViewById(R.id.luuPNH);
        btnAddPNH.setText("CẬP NHẬT");
    }

    public Boolean checkId(String id) {
        String emailPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,8}$";
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
        if (ApiUtils.isEmpty(txtInputTongTien)) {
            txtInputTongTien.setError("Tổng tiền không được để trống !");
            return false;
        }
        return true;
    }


    private PhieuNhapHang getPhieuNhapHang() {
        PhieuNhapHang CTPNH = new PhieuNhapHang();
        CTPNH.setId(txtInputIdPNH.getText().toString());
        CTPNH.setIdNV(String.valueOf(spinnerIDNV.getSelectedItem()));
        CTPNH.setTongTien(new BigDecimal(txtInputTongTien.getText().toString().trim()));
        CTPNH.setNgayLap(LocalDate.now().toString());
        return CTPNH;
    }

    public void updatePhieuNhapHang(PhieuNhapHang ct){
        apiService = ApiUtils.getApiService();
        apiService.updatePNH(ct).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 400){
                    Toast.makeText( getApplicationContext(),"ID Not found !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());

                }
                if(response.code() == 500){
                    Toast.makeText( getApplicationContext(),"Lỗi server ! không cập nhật đưuọc chi tiết phiếu nhập",
                            Toast.LENGTH_SHORT).show();

                }
                if(response.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Cập nhật thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListPNH.remove(curentPositionPNH);
                    tempListPNH.add(curentPositionPNH, getPhieuNhapHang());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setEvent(){
        btnAddPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isValidInput()){
                        Log.d("Current position", "onClick cập nhật: "+ curentPositionPNH);
                        updatePhieuNhapHang(getPhieuNhapHang());
                    }

            }
        });
    }

    public int getNhanVienPosition(String idnv) {
        for (int i = 0; i < listNhanVien.size(); i++) {
            if (idnv == listNhanVien.get(i).getId())
                return i;
        }
        return -1;
    }


    private void getPNHFromId(){
        apiService = ApiUtils.getApiService();
        apiService.getPNHById(tempListPNH.get(curentPositionPNH).getId()).enqueue(new Callback<PhieuNhapHang>() {
            @Override
            public void onResponse(Call<PhieuNhapHang> call, Response<PhieuNhapHang> response) {
                if(response.isSuccessful()){

                    PhieuNhapHang hh =response.body();
                    id = hh.getId();
                    txtInputIdPNH.setText(hh.getId());
                    spinnerIDNV.setSelection(getNhanVienPosition(hh.getIdNV()));
                    txtInputTongTien.setText(String.valueOf(hh.getTongTien()));
                    hh.setNgayLap(LocalDate.now().toString());
                }
                if(response.code() == 400){
                    Log.d("Không tìm thấy PNH với id = " + tempListPNH.get(curentPositionPNH).getId(),
                            "onResponse: ");
                }
                if(response.code() == 500){
                    Toast.makeText(getApplicationContext(),"Lỗi server : không thể lấy Phiếu nhập hàng từ id", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PhieuNhapHang> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Oopps! Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d("Lỗi ", "onFailure: " + t.getMessage());
            }
        });
    }
}
