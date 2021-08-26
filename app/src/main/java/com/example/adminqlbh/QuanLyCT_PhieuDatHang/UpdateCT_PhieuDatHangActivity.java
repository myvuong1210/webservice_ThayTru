package com.example.adminqlbh.QuanLyCT_PhieuDatHang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity.TAG;
import static com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity.curentPositionCTPDH;
import static com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity.tempListCTPDH;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.curentPosition;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.tempListHH;

public class UpdateCT_PhieuDatHangActivity extends AppCompatActivity {

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
        setControl();
        ActionBar();
        Log.d(TAG, "Current ID CTPN: " + tempListCTPDH.get(curentPositionCTPDH).getId());
        getCTPDHFromId();
        loadDataPNH();
        loadDataHangHoa();
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
                        Log.e("Không thể load được dữ liệu", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HangHoa>> call, Throwable t) {
                Log.e("Không thể load được dữ liệu", t.getMessage());
            }
        });
    }


    public void loadDataPNH() {
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
        txtInputId.setFocusable(false);
        txtInputId.setEnabled(false);
        txtInputId.setCursorVisible(false);
        txtInputThanhTien = findViewById(R.id.txtInputThanhTienCTPDH);
        txtInputSoluong = findViewById(R.id.txtInputSoluongCTPDH);
        spinnerIDHH = (Spinner) findViewById(R.id.spinnerIDHH_DH);
        spinnerIDPDH = (Spinner) findViewById(R.id.spinnerIDPDH);
        arrayAdapterHangHoa = new ArrayAdapter<HangHoa>(UpdateCT_PhieuDatHangActivity.this, android.R.layout.simple_spinner_item, listhangHoa);
        arrayAdapterPhieuDatHang = new ArrayAdapter<PhieuDatHang>(UpdateCT_PhieuDatHangActivity.this, android.R.layout.simple_spinner_dropdown_item, listPhieuDatHang);
        arrayAdapterHangHoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPhieuDatHang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIDPDH.setAdapter(arrayAdapterPhieuDatHang);
        spinnerIDHH.setAdapter(arrayAdapterHangHoa);
        toolbar=findViewById(R.id.toolbar);
        btnAddCTPDH = findViewById(R.id.luuCTPDH);
        btnAddCTPDH.setText("CẬP NHẬT");
    }


    //====================Validate======================
    public boolean isValidInput() {
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
    public void updateCT_PhieuDatHang(CT_PhieuDatHang ct){
        apiService = ApiUtils.getApiService();
        apiService.updateCTPDH(ct).enqueue(new Callback<String>() {
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
                    // Sau khi server response thêm giá niêm yết ngày hiện tại thành công
                    // Cập nhật danh sách tạm và thoát activity
                    tempListCTPDH.remove(curentPositionCTPDH);
                    tempListCTPDH.add(curentPositionCTPDH, getCT_PhieuDatHang());
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
        btnAddCTPDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    Log.d("Current position", "onClick cập nhật: "+ curentPositionCTPDH);
                    updateCT_PhieuDatHang(getCT_PhieuDatHang());
                }

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
        for (int i = 0; i < listPhieuDatHang.size(); i++) {
            if (idDonhang == listPhieuDatHang.get(i).getId())
                return i;
        }
        return -1;
    }

    private void getCTPDHFromId() {
        CT_PhieuDatHang hh = tempListCTPDH.get(curentPositionCTPDH);
        txtInputId.setText(hh.getId());
        spinnerIDPDH.setSelection(getDonHangPosition(hh.getIdPhieuDatHang()));
        spinnerIDHH.setSelection(getHangHoaPosition(hh.getIdHH()));
        txtInputSoluong.setText(String.valueOf(hh.getSoLuong()));
        txtInputThanhTien.setText(String.valueOf(hh.getThanhTien()));
    }
}
