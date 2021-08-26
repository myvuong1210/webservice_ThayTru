package com.example.adminqlbh.QuanLyCT_PhieuNhapHang;

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
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.PhieuNhapHang;
import com.example.adminqlbh.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity.TAG;
import static com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity.curentPositionCTPNH;
import static com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity.tempListCTPNH;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.curentPosition;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.tempListHH;

public class UpdateCT_PhieuNhapHangActivity extends AppCompatActivity {

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
        setControl();
        ActionBar();
        Log.d(TAG, "Current ID CTPN: " + tempListCTPNH.get(curentPositionCTPNH).getId());
        getCTPNHFromId();
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
        txtInputId.setFocusable(false);
        txtInputId.setEnabled(false);
        txtInputId.setCursorVisible(false);
        txtInputThanhTien = findViewById(R.id.txtInputThanhTienCTPNH);
        txtInputSoluong = findViewById(R.id.txtInputSoluongCTPNH);
        spinnerIDHH = (Spinner) findViewById(R.id.spinnerIDHH);
        spinnerIDPNH = (Spinner) findViewById(R.id.spinnerIDPNH);
        arrayAdapterHangHoa = new ArrayAdapter<HangHoa>(UpdateCT_PhieuNhapHangActivity.this, android.R.layout.simple_spinner_item, listhangHoa);
        arrayAdapterPhieuNhapHang = new ArrayAdapter<PhieuNhapHang>(UpdateCT_PhieuNhapHangActivity.this, android.R.layout.simple_spinner_dropdown_item, listPhieuNhapHang);
        arrayAdapterHangHoa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterPhieuNhapHang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIDPNH.setAdapter(arrayAdapterPhieuNhapHang);
        spinnerIDHH.setAdapter(arrayAdapterHangHoa);
        toolbar=findViewById(R.id.toolbar);
        btnAddCTPNH = findViewById(R.id.luuCTPNH);
        btnAddCTPNH.setText("CẬP NHẬT");
    }

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


    private CT_PhieuNhapHang getCT_PhieuNhapHang() {
        CT_PhieuNhapHang CTPNH = new CT_PhieuNhapHang();
        CTPNH.setId(txtInputId.getText().toString());
        CTPNH.setIdhh(String.valueOf(spinnerIDHH.getSelectedItem()));
        CTPNH.setIdPhieuNhapHang(String.valueOf(spinnerIDPNH.getSelectedItem()));
        CTPNH.setSoLuong(Integer.valueOf(txtInputSoluong.getText().toString().trim()));
        CTPNH.setThanhTien(Integer.valueOf(txtInputThanhTien.getText().toString().trim()));
        return CTPNH;
    }
    public void updateCT_PhieuNhapHang(CT_PhieuNhapHang ct){
        apiService = ApiUtils.getApiService();
        apiService.updateCTPNH(ct).enqueue(new Callback<String>() {
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
                    tempListCTPNH.remove(curentPositionCTPNH);
                    tempListCTPNH.add(curentPositionCTPNH, getCT_PhieuNhapHang());
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
        btnAddCTPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(isValidInput()){
                        Log.d("Current position", "onClick cập nhật: "+ curentPositionCTPNH);
                        updateCT_PhieuNhapHang(getCT_PhieuNhapHang());
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
        for (int i = 0; i < listPhieuNhapHang.size(); i++) {
            if (idDonhang == listPhieuNhapHang.get(i).getId())
                return i;
        }
        return -1;
    }

    private void getCTPNHFromId() {
        CT_PhieuNhapHang hh = tempListCTPNH.get(curentPositionCTPNH);
        txtInputId.setText(hh.getId());
        spinnerIDPNH.setSelection(getDonHangPosition(hh.getIdPhieuNhapHang()));
        spinnerIDHH.setSelection(getHangHoaPosition(hh.getIdhh()));
        txtInputSoluong.setText(String.valueOf(hh.getSoLuong()));
        txtInputThanhTien.setText(String.valueOf(hh.getThanhTien()));
    }
}
