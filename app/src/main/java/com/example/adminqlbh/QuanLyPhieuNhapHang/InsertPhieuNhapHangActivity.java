
package com.example.adminqlbh.QuanLyPhieuNhapHang;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.HangHoa;
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

import static com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity.tempListPNH;

public class InsertPhieuNhapHangActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputTongTien;
    private Button btnAddPNH;
    private ApiService apiService;

    ArrayAdapter<NhanVien> arrayAdapterNhanVien;

    Spinner spinnerIDNV;
    ArrayList<NhanVien> listNhanVien = new ArrayList<>();
    private String TAG;
    private boolean isInsertSuccess;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_phieunhaphang);
        loadDataNhanVien();
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
        txtInputId = findViewById(R.id.txtInputId_PNH);
        txtInputTongTien = findViewById(R.id.txtInputTongTienPNH);
        spinnerIDNV= (Spinner) findViewById(R.id.spinnerIDNV);
        arrayAdapterNhanVien = new ArrayAdapter<NhanVien>(InsertPhieuNhapHangActivity.this, android.R.layout.simple_spinner_item, listNhanVien);
        spinnerIDNV.setAdapter(arrayAdapterNhanVien);
        toolbar=findViewById(R.id.toolbar);
        btnAddPNH = findViewById(R.id.luuPNH);
        btnAddPNH.setText("THÊM");
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
        if (ApiUtils.isEmpty(txtInputTongTien)) {
            txtInputTongTien.setError("Tổng tiền không được để trống !");
            return false;
        }
        return true;
    }



    private PhieuNhapHang getPhieuNhapHang() {
        PhieuNhapHang CTPNH = new PhieuNhapHang();
        CTPNH.setId(txtInputId.getText().toString().toUpperCase());
        CTPNH.setIdNV(String.valueOf(spinnerIDNV.getSelectedItem()));
        CTPNH.setTongTien(new BigDecimal(txtInputTongTien.getText().toString().trim()));
        CTPNH.setNgayLap(LocalDate.now().toString());
        return CTPNH;
    }

    private void insertPhieuNhapHang() {
        apiService = ApiUtils.getApiService();
        apiService.postPhieuNhapHang(getPhieuNhapHang()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListPNH.add(getPhieuNhapHang());
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
                Log.d("Thêm phiếu nhập hàng ", "onFailure: " + t.getMessage());
                isInsertSuccess = false;
            }
        });
    }

    private void setEvent() {
        btnAddPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidInput()) {
                    insertPhieuNhapHang();
                }
            }
        });

    }
}



