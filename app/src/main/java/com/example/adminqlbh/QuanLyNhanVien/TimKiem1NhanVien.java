package com.example.adminqlbh.QuanLyNhanVien;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TimKiem1NhanVien extends AppCompatActivity {

    EditText edtIdNV;
    TextView edtTenNV, edtEmail, edtDiachi, edtSdt,edtQuyen;
    Button btnXemCTNV;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timkiem1nhanvien);
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
        btnXemCTNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLy();
            }


        });
    }
    private void xuLy() {
        ChiTietNhanVienTask task=new ChiTietNhanVienTask();
        task.execute(edtIdNV.getText().toString());
    }

    private void addControls() {
        edtIdNV=(EditText) findViewById(R.id.txtIdNV_TK);
        edtDiachi=(TextView) findViewById(R.id.txtDiachiNV_TK);
        edtSdt=(TextView) findViewById(R.id.txtSdtNV_TK);
        edtEmail=(TextView) findViewById(R.id.txtEmail_TK);
        edtTenNV=(TextView) findViewById(R.id.txtTenNV_TK);
        edtQuyen=findViewById(R.id.txtQuyen);
        toolbar=findViewById(R.id.toolbar);
        btnXemCTNV=(Button) findViewById(R.id.btnXemCTNV);


    }

    class ChiTietNhanVienTask extends AsyncTask<String, Void, NhanVien>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(NhanVien NhanVien) {

            super.onPostExecute(NhanVien);
            if(NhanVien!=null)
            {
                edtIdNV.setText(NhanVien.getId()+"");
                edtTenNV.setText(NhanVien.getHoTen());
                edtDiachi.setText(NhanVien.getDiaChi()+"");
                edtSdt.setText(NhanVien.getSdt()+"");
                edtEmail.setText(NhanVien.getEmail()+"");

                if(NhanVien.getQuyen()==true) {
                    edtQuyen.setText("Mã nhân viên có quyền là Admin");
                }
                else edtQuyen.setText("Mã nhân viên có quyền là Nhân viên");
            }
            else
            {
                Toast.makeText(TimKiem1NhanVien.this,"NVông tìm thấy mã Nhân viên! Xin nhập lại mã Nhân viên mới",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected NhanVien doInBackground(String... strings) {

            try {
                URL url = new URL( "http://116.109.64.101:8080/nhanvien/"+strings[0]);
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

                String idNV = jsonObject.getString("id");
                String tenNV = jsonObject.getString("hoTen");
                String diachi = jsonObject.getString("diaChi");
                String sdt = jsonObject.getString("sdt");
                String email = jsonObject.getString("email");
                Boolean quyen=jsonObject.getBoolean("quyen");

                NhanVien NV = new NhanVien();
                NV.setId(idNV);
                NV.setHoTen(tenNV);
                NV.setDiaChi(diachi);
                NV.setSdt(sdt);
                NV.setEmail(email);
                NV.setQuyen(quyen);
                return NV;
            } catch (Exception e) {
                Log.e("LOI", e.toString());
            }
            return null;
        }
    }
}
