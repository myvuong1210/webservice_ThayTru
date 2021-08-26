
package com.example.adminqlbh.QuanLyKhachHang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.adminqlbh.Models.CT_GiaNiemYet;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.BreakIterator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.TAG;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.curentPositionKH;
import static com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity.tempListKH;

public class InsertKhachHangActivity extends AppCompatActivity {

    private static String TAG;
    private EditText txtInputId, txtInputTenKH,
            txtInputEmail, txtInputSdt, txtInputDiachi, txtInputMatkhau;
    private Button btnAddKH;
    private ApiService apiService;
    private boolean isInsertSuccess = false;
    private Toolbar toolbar;
    private String params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_khachhang);
        setControl();
        createIdKhacHang();
        ActionBar();
        setEvent();
    }

    private void setControl(){
        txtInputTenKH = findViewById(R.id.txtTenKH);
        txtInputEmail = findViewById(R.id.txtEmailKH);
        txtInputSdt = findViewById(R.id.txtSdtKH);
        txtInputDiachi = findViewById(R.id.txtDiachiKH);
        btnAddKH = findViewById(R.id.luuKH);
        toolbar=findViewById(R.id.toolbar);
        btnAddKH.setText("THÊM");
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

    private void createIdKhacHang(){
        apiService = ApiUtils.getApiService();
        apiService.getListKhachHang().enqueue(new Callback<List<KhachHang>>() {
            @Override
            public void onResponse(Call<List<KhachHang>> call, Response<List<KhachHang>> response) {
                if(response.isSuccessful()){
                    int soLuong = response.body().size() + 1;  // Tăng số lượng lên 1 để lấy id tiếp theo
                    if (soLuong < 10)
                        params = "KH00" + soLuong;
                    else
                    {
                        if (soLuong < 100)
                        {
                            params = "KH0" + soLuong;
                        }
                        else
                        {
                            params = "KH" + soLuong;
                        }
                    }
                }
                else {
                    Errors.initListError();
                    Toast.makeText(InsertKhachHangActivity.this, Errors.listErrors.get(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KhachHang>> call, Throwable t) {
                Toast.makeText(InsertKhachHangActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private KhachHang getKhachHang(){
        KhachHang hh = new KhachHang();
        hh.setId(params);
        hh.setHoTen(txtInputTenKH.getText().toString());
        hh.setEmail(txtInputEmail.getText().toString());
        hh.setSdt(txtInputSdt.getText().toString());
        hh.setDiaChi(txtInputDiachi.getText().toString());
        hh.setMatkhau("123");
        return hh;
    }
    private void insertKhachHang(){
        apiService = ApiUtils.getApiService();
        apiService.postKhachHang(getKhachHang()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    tempListKH.remove(curentPositionKH);
                    tempListKH.add(curentPositionKH, getKhachHang());
                    finish();
                }
                if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "",
                            Toast.LENGTH_SHORT).show();
                    isInsertSuccess = false;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d(HangHoaActivity.TAG, "onFailure: " + t.getMessage());
                isInsertSuccess = false;
            }
        });
    }

    private void setEvent(){
        btnAddKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    insertKhachHang();
                }
               }
        });

    }
}
