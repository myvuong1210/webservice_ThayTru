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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.LoginActivity;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.TAG;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.curentPositionNV;
import static com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity.tempListNV;

public class Xem1NhanVienActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputTenNV, txtInputEmail,
            txtInputSdt, txtInputDiachi;
    private Button btnAddNV;
    private ApiService apiService;
    private Toolbar toolbar;
    private boolean isUpdateSuccess;
    private RadioButton quyenAdmin, quyenNhanVien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xem1nhanvien);
        setControl();
        getNhanVienFromId();
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

    public void setControl(){
        txtInputId = findViewById(R.id.txtIdNV);
        txtInputId.setFocusable(false);
        txtInputId.setEnabled(false);
        txtInputId.setCursorVisible(false);
        txtInputTenNV = findViewById(R.id.txtTenNV);
        txtInputEmail = findViewById(R.id.txtEmailNV);
        txtInputSdt = findViewById(R.id.txtSdtNV);
        txtInputDiachi = findViewById(R.id.txtDiachiNV);
        RadioGroup group=(RadioGroup) findViewById(R.id.radioGroup);
        quyenAdmin=(RadioButton) findViewById(R.id.quyenAdmin);
        quyenNhanVien=(RadioButton) findViewById(R.id.quyenNV);
        toolbar=findViewById(R.id.toolbar);
        btnAddNV = findViewById(R.id.capnhatNV);
        btnAddNV.setText("SỬA THÔNG TIN TÀI KHOẢN");
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
        return isValid;
    }

    private void getNhanVienFromId(){
        apiService = ApiUtils.getApiService();
        apiService.getNhanVienById(LoginActivity.username).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if(response.isSuccessful()){
                    NhanVien hh = response.body();
                    txtInputId.setText(hh.getId().toUpperCase().trim());
                    txtInputTenNV.setText(hh.getHoTen());
                    txtInputEmail.setText(hh.getEmail());
                    txtInputSdt.setText(hh.getSdt());
                    txtInputDiachi.setText(hh.getDiaChi());
                    if(hh.getQuyen()==true)
                    {
                        quyenAdmin.setChecked(true);

                    }
                    if(hh.getQuyen()==false)
                    {
                        quyenNhanVien.setChecked(true);

                    }
                }
                if(response.code() == 400){
                    Log.d("Không tìm thấy nhân viên với id = " + tempListNV.get(curentPositionNV).getId(),
                            "onResponse: ");
                }
                if(response.code() == 500){
                    Toast.makeText(getApplicationContext(),"Lỗi server : không thể lấy nhân viên từ id", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Oopps! Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d("Lỗi ", "onFailure: " + t.getMessage());
            }
        });
    }

    public void setEvent(){
        btnAddNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Xem1NhanVienActivity.this, Update1NhanVienActivity.class);
                startActivity(intent);
            }
        });
    }
}
