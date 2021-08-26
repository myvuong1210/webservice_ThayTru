package com.example.adminqlbh.QuanLyKhachHang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.curentPosition;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.TAG;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.curentPositionKH;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.tempListKH;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.curentPositionNV;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.tempListNV;
import static com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity.curentPositionPNH;
import static com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity.tempListPNH;

public class  UpdateKhachHangActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputTenKH, txtInputEmail,
            txtInputSdt, txtInputDiachi;
    private Button btnAddKH;
    private ApiService apiService;
    private String id;
    private boolean isUpdateSuccess = false;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_khachhang);

        setControl();
        ActionBar();
        getKhachHangFromId();
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

    public void setControl(){
        txtInputId = findViewById(R.id.txtIdKH);
        txtInputId.setFocusable(false);
        txtInputId.setEnabled(false);
        txtInputId.setCursorVisible(false);
        txtInputTenKH = findViewById(R.id.txtTenKH);
        txtInputEmail = findViewById(R.id.txtEmailKH);
        txtInputSdt = findViewById(R.id.txtSdtKH);
        txtInputDiachi = findViewById(R.id.txtDiachiKH);
        toolbar=findViewById(R.id.toolbar);
        btnAddKH = findViewById(R.id.luuKH);
        btnAddKH.setText("CẬP NHẬT");
    }

    public Boolean checkEmail(String email) {
        String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkKitu(String email) {
        String emailPattern = "^[\\p{L} .'-]+$";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean checkDiachi(String email) {
        String emailPattern = "\\w+";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(email);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean checkSdt(String sdt) {
        String emailPattern = "0[0-9]{9,10}";
        Pattern regex = Pattern.compile(emailPattern);
        Matcher matcher = regex.matcher(sdt);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }



    //====================Validate======================
    public boolean isValidInput(){
        boolean isValid = true;

        if(checkKitu(txtInputTenKH.getText().toString())==false){
            txtInputTenKH.setError("Tên Khách hàng không hợp lệ, xin thử lại !");
            isValid = false;
        }

        if(checkDiachi(txtInputDiachi.getText().toString())==false){
            txtInputDiachi.setError("Địa chỉ khách hàng không hợp lệ, xin thử lại ! ");
            isValid = false;
        }
        if(checkSdt(txtInputSdt.getText().toString())==false){
            txtInputSdt.setError("Số điện thoại không hợp lệ, xin vui lòng nhập lại số điện thoại khác !");
            isValid = false;
        }

        if(checkEmail(txtInputEmail.getText().toString())==false){
            txtInputEmail.setError("Email khách hàng không hợp lệ, xin vui lòng nhập lại!");
            isValid = false;
        }
        return isValid;
    }


    private KhachHang getKhachHang(){
        KhachHang hh = new KhachHang();
        hh.setId(txtInputId.getText().toString().toUpperCase().trim());
        hh.setHoTen(txtInputTenKH.getText().toString().trim());
        hh.setEmail(txtInputEmail.getText().toString().trim());
        hh.setSdt(txtInputSdt.getText().toString().trim());
        hh.setDiaChi(txtInputDiachi.getText().toString().trim());
        hh.setMatkhau("123");
        return hh;
    }
    public void upadteKhachHang(KhachHang hh){
        apiService = ApiUtils.getApiService();
        apiService.updateKH(hh).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 400){
                    Toast.makeText( getApplicationContext(),"ID Not found !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    isUpdateSuccess = false;
                }
                if(response.code() == 500){
                    Toast.makeText( getApplicationContext(),"Lỗi server ! không cập nhật đưuọc hàng hóa",
                            Toast.LENGTH_SHORT).show();
                    isUpdateSuccess = false;
                }
                if(response.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Cập nhật thành công !",
                            Toast.LENGTH_SHORT).show();
                    tempListKH.remove(curentPositionKH);
                    tempListKH.add(curentPositionKH, getKhachHang());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
                isUpdateSuccess = false;
            }
        });
    }

    private void getKhachHangFromId(){
        apiService = ApiUtils.getApiService();
        apiService.getKhachHangById(tempListKH.get(curentPositionKH).getId()).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.isSuccessful()){
                    KhachHang hh = response.body();
                    id = hh.getId();
                    txtInputId.setText(hh.getId().trim().toUpperCase().trim());
                    txtInputTenKH.setText(hh.getHoTen().trim());
                    txtInputEmail.setText(hh.getEmail().trim());
                    txtInputSdt.setText(hh.getSdt().trim());
                    txtInputDiachi.setText(hh.getDiaChi().trim());
                    hh.setMatkhau("12");
                }
                if(response.code() == 400){
                    Log.d("Không tìm thấy KH với id = " + tempListKH.get(curentPositionKH).getId(),
                            "onResponse: ");
                }
                if(response.code() == 500){
                    Toast.makeText(getApplicationContext(),"Lỗi server : không thể lấy KH từ id", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Oopps! Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d("Lỗi ", "onFailure: " + t.getMessage());
            }
        });
    }

    public void setEvent(){
        btnAddKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    Log.d("Current position", "onClick cập nhật: "+ curentPositionKH);
                    upadteKhachHang(getKhachHang());
                }
            }
        });
    }
}
