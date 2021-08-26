package com.example.adminqlbh.QuanLyKhachHang;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TimKiem1KhachHang extends AppCompatActivity {

    EditText edtIdKH;
    TextView edtTenKH, edtEmail, edtDiachi, edtSdt;
    Button btnXemCTKH;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timkiem1khachhang);
        addControls();
        ActionBar();
        addEvent();
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

        private void addEvent() {
        btnXemCTKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               xuLy();
            }


        });
    }
    private void xuLy() {
        ChiTietKhachHangTask task=new ChiTietKhachHangTask();
        task.execute(edtIdKH.getText().toString());
    }

    private void addControls() {
        edtIdKH=(EditText) findViewById(R.id.txtIdKH_TK);
        edtDiachi=(TextView) findViewById(R.id.txtDiachiKH_TK);
        edtSdt=(TextView) findViewById(R.id.txtSdtKH_TK);
        edtEmail=(TextView) findViewById(R.id.txtEmail_TK);
        edtTenKH=(TextView) findViewById(R.id.txtTenKH_TK);
        toolbar=findViewById(R.id.toolbar);
        btnXemCTKH=(Button) findViewById(R.id.btnXemCTKH);


    }

    class ChiTietKhachHangTask extends AsyncTask<String, Void, KhachHang>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(KhachHang khachHang) {

            super.onPostExecute(khachHang);
            if(khachHang!=null)
            {
                edtIdKH.setText(khachHang.getId()+"");
                edtTenKH.setText(khachHang.getHoTen());
                edtDiachi.setText(khachHang.getDiaChi()+"");
                edtSdt.setText(khachHang.getSdt()+"");
                edtEmail.setText(khachHang.getEmail()+"");
            }
            else
            {
                Toast.makeText(TimKiem1KhachHang.this,"Không tìm thấy mã Khách hàng! Xin nhập lại mã Khách hàng mới",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected KhachHang doInBackground(String... strings) {

            try {
                URL url = new URL( "http://116.109.64.101:8080/khachhang/"+strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject jsonObject = new JSONObject(builder.toString());

                String idKH = jsonObject.getString("id");
                String tenKH = jsonObject.getString("hoTen");
                String diachi = jsonObject.getString("diaChi");
                String sdt = jsonObject.getString("sdt");
                String email = jsonObject.getString("email");

                KhachHang kh = new KhachHang();
                kh.setId(idKH);
                kh.setHoTen(tenKH);
                kh.setDiaChi(diachi);
                kh.setSdt(sdt);
                kh.setEmail(email);
                return kh;
            } catch (Exception e) {
                Log.e("LOI", e.toString());
            }
            return null;
        }
    }
}
