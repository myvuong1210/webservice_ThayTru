
package com.example.adminqlbh.QuanLyNhanVien;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.QuanLyKhachHang.InsertKhachHangActivity;
import com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity;
import com.example.adminqlbh.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.curentPositionKH;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.tempListKH;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.curentPositionNV;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.tempListNV;

public class InsertNhanVienActivity extends AppCompatActivity {

    private EditText txtInputTenNV,
            txtInputEmail, txtInputSdt, txtInputDiachi;
    private Button btnAddNV;
    RadioButton quyenAdmin, quyenNhanVien;
    private ApiService apiService;
    private boolean isInsertSuccess = false;
    private Toolbar toolbar;
    private String params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_nhanvien);
        setControl();
        createNhanVienId();
        ActionBar();
        setEvent();
    }

    private void setControl(){
        txtInputTenNV = findViewById(R.id.txtTenNV);
        txtInputEmail = findViewById(R.id.txtEmailNV);
        txtInputSdt = findViewById(R.id.txtSdtNV);
        txtInputDiachi = findViewById(R.id.txtDiachiNV);
        toolbar=findViewById(R.id.toolbar);

        RadioGroup group=(RadioGroup) findViewById(R.id.radioGroup);
        quyenAdmin=(RadioButton) findViewById(R.id.quyenAdmin);
        quyenNhanVien=(RadioButton) findViewById(R.id.quyenNV);
        btnAddNV = findViewById(R.id.luuNV);
        toolbar=findViewById(R.id.toolbar);
        btnAddNV.setText("THÊM");
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
        boolean isValid = true;

        if(checkKitu(txtInputTenNV.getText().toString())==false){
            txtInputTenNV.setError("Tên Nhân viên không hợp lệ, xin thử lại !");
            isValid = false;
        }

        if(checkDiachi(txtInputDiachi.getText().toString())==false){
            txtInputDiachi.setError("Địa chỉ Nhân viên không hợp lệ, xin thử lại ! ");
            isValid = false;
        }

        if(checkSdt(txtInputSdt.getText().toString())==false){
            txtInputSdt.setError("Số điện thoại không hợp lệ, xin vui lòng nhập lại số điện thoại khác !");
            isValid = false;
        }

        if(checkEmail(txtInputEmail.getText().toString())==false){
            txtInputEmail.setError("Email Nhân viên không hợp lệ, xin vui lòng nhập lại!");
            isValid = false;
        }

        RadioGroup group=(RadioGroup) findViewById(R.id.radioGroup);
        int id=group.getCheckedRadioButtonId();
        if(id==-1)
        {
            Toast.makeText(this, "Phải chọn quyền", Toast.LENGTH_LONG).show();
            isValid=false;
        }
        return isValid;
    }

    private NhanVien getNhanVien(){
        NhanVien hh = new NhanVien();
        hh.setId(params);
        hh.setHoTen(txtInputTenNV.getText().toString());
        hh.setEmail(txtInputEmail.getText().toString());
        hh.setSdt(txtInputSdt.getText().toString());
        hh.setDiaChi(txtInputDiachi.getText().toString());

        if(quyenAdmin.isChecked())
        {
           hh.setQuyen(true);
        }
        if(quyenNhanVien.isChecked())
        {
            hh.setQuyen(false);
        }
        hh.setMatkhau("123");
        return hh;
    }


    private void createNhanVienId(){
        apiService = ApiUtils.getApiService();
        apiService.getListNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if(response.isSuccessful()){
                    int soLuong = response.body().size()+1;  // Tăng số lượng lên 1 để lấy id tiếp theo
                    if (soLuong < 10)
                        params = "NV00" + soLuong;
                    else
                    {
                        if (soLuong < 100)
                        {
                            params = "NV0" + soLuong;
                        }
                        else
                        {
                            params = "NV" + soLuong;
                        }
                    }
                }
                else {
                    Errors.initListError();
                    Toast.makeText(InsertNhanVienActivity.this, Errors.listErrors.get(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Toast.makeText(InsertNhanVienActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void insertNhanVien(){
        apiService = ApiUtils.getApiService();
        apiService.postNhanVien(getNhanVien()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Thêm Nhân viên thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListNV.remove(curentPositionNV);
                    tempListNV.add(curentPositionNV, getNhanVien());
                    finish();

                }
                if(response.code() == 400){
                    Toast.makeText( getApplicationContext(),"ID đã tồn tại !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: ID đã tồn tại !" + response.body());
                    isInsertSuccess = false;

                }
                if(response.code() == 500){
                    Toast.makeText( getApplicationContext(),"Lỗi server !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: Lỗi server !" + response.body());
                    isInsertSuccess = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Thêm Nhân viên ", "onFailure: " + t.getMessage());
                isInsertSuccess = false;
            }
        });

    }


    private void setEvent(){
        btnAddNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    insertNhanVien();
                    ApiUtils.delay(4, new ApiUtils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            if(isInsertSuccess){
                                tempListNV.add(getNhanVien());
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }
}
