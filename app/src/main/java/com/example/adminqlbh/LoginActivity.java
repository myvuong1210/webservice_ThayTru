package com.example.adminqlbh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.Login;
import com.example.adminqlbh.UserSite.activity.UserHomePageActivity;
import com.example.adminqlbh.Util.InternetConnnection;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_loginName, editText_password;
    private Button btnLogin;
    private ApiService apiService;
    private int roleID;
    public static String username, tenKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title_bar
        setContentView(R.layout.layout_login);
        Errors.initListError();
        setControl();
        setEvent();
    }

    private void setControl() {
        editText_loginName = findViewById(R.id.editText_usernameLogin);
        editText_password = findViewById(R.id.editText_passwordLogin);
        btnLogin = findViewById(R.id.btn_dangnhap);
    }

    private void setEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InternetConnnection.haveNetworkConnection(LoginActivity.this)){
                    if(isValidLogin()){
                        sign_in();
                    }
                }
                else Toast.makeText(LoginActivity.this, "Kiểm tra kết nối mạng !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Login getLogin(){
        Login account = new Login();
        account.setUsername(editText_loginName.getText().toString().trim());
        account.setPassword(editText_password.getText().toString().trim());
        return account;
    }

    private void sign_in() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.sign_in(getLogin()).enqueue((new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    roleID = response.body();
                    username = getLogin().getUsername();
                    if(roleID == -1){
                        Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại hoặc nhập sai mật khẩu" +
                                "\nHãy đăng nhập lại", Toast.LENGTH_SHORT).show();
                    }
                    if(roleID == 1 || roleID == 0){
                        if(roleID == 0){
                            Toast.makeText(LoginActivity.this, "Đăng nhập tài khoản nhân viên",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(roleID == 1){
                            Toast.makeText(LoginActivity.this, "Đăng nhập tài khoản admin",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("ROLE ID", roleID);
                        startActivity(intent);
                    }
                    if (roleID == 2){
                        Toast.makeText(LoginActivity.this, "Đăng nhập tài khoản khách hàng",
                                Toast.LENGTH_SHORT).show();
                        getLoginName();
                        Intent intent = new Intent(LoginActivity.this, UserHomePageActivity.class);
                        intent.putExtra("ROLE ID", roleID);
                        startActivity(intent);
                    }
                    pd.dismiss();
                }
                else{
                    try {
                        String errorMessage = Errors.listErrors.get(response.code());
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.d("Lỗi ", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }));
    }
    private void getLoginName(){
        apiService = ApiUtils.getApiService();
        apiService.getKhachHangById(LoginActivity.username).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if(response.isSuccessful()){
                    try {
                        tenKhachHang = response.body().getHoTen();

                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    try{
                        String errorMessage = Errors.listErrors.get(response.code());
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidLogin(){
        if(ApiUtils.isEmpty(editText_loginName)){
            editText_loginName.setError("Tên đăng nhập không để để trống !");
            return false;
        }
        if (ApiUtils.isEmpty(editText_password)){
            editText_password.setError("Mật khẩu không được để trống !");
            return false;
        }
        return true;
    }
}
