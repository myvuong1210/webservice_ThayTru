package com.example.adminqlbh.QuanLyNhanVien;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.TwoStatePreference;
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
import com.example.adminqlbh.Api.AsyncTaskDownloadImage;
import com.example.adminqlbh.Models.HangHoa;
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

public class UpdateNhanVienActivity extends AppCompatActivity {

    private EditText txtInputId, txtInputTenNV, txtInputEmail,
            txtInputSdt, txtInputDiachi;
    private Button btnAddNV;
    private ApiService apiService;
    private String idNV;
    private boolean isUpdateSuccess = false;
    private RadioButton quyenAdmin, quyenNhanVien;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_nhanvien);
        setControl();
        ActionBar();
        setEvent();
        getNhanVienFromId();
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
        btnAddNV = findViewById(R.id.luuNV);
        btnAddNV.setText("CẬP NHẬT");
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
        hh.setId(txtInputId.getText().toString().toUpperCase().trim());
        hh.setHoTen(txtInputTenNV.getText().toString().trim());
        hh.setEmail(txtInputEmail.getText().toString().trim());
        hh.setSdt(txtInputSdt.getText().toString().trim());
        hh.setDiaChi(txtInputDiachi.getText().toString().trim());

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
    public void upadteNhanVien(NhanVien hh){
        apiService = ApiUtils.getApiService();
        apiService.updateNV(hh).enqueue(new Callback<String>() {
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
                    Log.d("Response body update", "onResponse: " + response.body());
                    tempListNV.remove(curentPositionNV);
                    tempListNV.add(curentPositionNV, getNhanVien());
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

    private void getNhanVienFromId(){
        apiService = ApiUtils.getApiService();
        apiService.getNhanVienById(tempListNV.get(curentPositionNV).getId()).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if(response.isSuccessful()){
                    NhanVien hh = response.body();
                    idNV = hh.getId();
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
                if(isValidInput()){
                    Log.d("Current position", "onClick cập nhật: "+ curentPositionNV);
                    upadteNhanVien(getNhanVien());
                    // Wait to update state variable isUpdateSuccess
                    ApiUtils.delay(4, new ApiUtils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            if(isUpdateSuccess){
                                Log.d("Cập nhật thành công ", "afterDelay: vị trí "
                                        + curentPositionNV + " và sp được thêm : " + tempListNV.get(curentPositionNV));
                                tempListNV.remove(curentPositionNV);
                                tempListNV.add(curentPositionNV, getNhanVien());
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}
