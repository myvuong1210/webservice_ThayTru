package com.example.adminqlbh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.adminqlbh.Fragment.DonHangFragment;
import com.example.adminqlbh.Fragment.HangHoaFragment;
import com.example.adminqlbh.Fragment.NhanVienFragment;
import com.example.adminqlbh.Fragment.TaiKhoanFragment;
import com.example.adminqlbh.Fragment.TrangChuFragment;
import com.example.adminqlbh.QuanLyCT_PhieuDatHang.CT_PhieuDatHangActivity;
import com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangActivity;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity;
import com.example.adminqlbh.QuanLyKhachHang.TimKiem1KhachHang;
import com.example.adminqlbh.QuanLyNhanVien.NhanVienActivity;
import com.example.adminqlbh.QuanLyNhanVien.TimKiem1NhanVien;
import com.example.adminqlbh.QuanLyNhanVien.Update1NhanVienActivity;
import com.example.adminqlbh.QuanLyNhanVien.Xem1NhanVienActivity;
import com.example.adminqlbh.QuanLyPhieuDatHang.InsertPhieuDatHangActivity;
import com.example.adminqlbh.QuanLyPhieuDatHang.PhieuDatHangActivity;
import com.example.adminqlbh.QuanLyPhieuNhapHang.PhieuNhapHangActivity;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class MainActivity extends AppCompatActivity {
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    

    Toolbar toolbar;
    Button btnQLHH, btnThemPDH, btnQL_PhieuNhapHang, btnTK, btnXem1NV;
    Button btnQLCTPNH, btnQLKH, btnQLNV, btnQLCTPDH, btnQL_PhieuDatHang;
    CardView cardViewBtnQLNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        ActionBar();
        setEvent();
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_logout);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setControl() {
        int roleNumber = getIntent().getIntExtra("ROLE ID", -1);
        btnQLHH = findViewById(R.id.btnQLHH);
        btnQL_PhieuNhapHang = findViewById(R.id.btn_QL_PhieuNhapHang);
        btnQL_PhieuDatHang = findViewById(R.id.btn_QL_PhieuDatHang);
        btnQLKH= findViewById(R.id.btnQLKH);
        btnQLNV= findViewById(R.id.btnQLNV);
        cardViewBtnQLNV = findViewById(R.id.cardViewBtnQLNV);
        btnQLCTPNH= findViewById(R.id.btnQLCTPNH);
        btnQLCTPDH= findViewById(R.id.btnQLCTPDH);
        btnTK = findViewById(R.id.btnTK);
        btnThemPDH=findViewById(R.id.btnThemPDH);
        toolbar=findViewById(R.id.toolbar);
        // nhân viên
        if(roleNumber == 0){
            btnQLNV.setEnabled(false);
            cardViewBtnQLNV.setVisibility(View.GONE);
            btnQLNV.setVisibility(View.GONE);
        }
        // admin
        if(roleNumber == 1){
            btnQLNV.setEnabled(true);
            cardViewBtnQLNV.setVisibility(View.VISIBLE);
            btnQLNV.setVisibility(View.VISIBLE);
        }

    }

    private void setEvent(){


        btnQLHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HangHoaActivity.class);
                startActivity(intent);
            }
        });

        btnQLCTPDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CT_PhieuDatHangActivity.class);
                startActivity(intent);
            }
        });

        btnQL_PhieuDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhieuDatHangActivity.class);
                startActivity(intent);
            }
        });

        btnQL_PhieuNhapHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhieuNhapHangActivity.class);
                startActivity(intent);
            }
        });

        btnQLKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KhachHangActivity.class);
                startActivity(intent);
            }
        });

        btnQLCTPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CT_PhieuNhapHangActivity.class);
                startActivity(intent);
            }
        });

        btnQLNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NhanVienActivity.class);
                startActivity(intent);
            }
        });

        btnTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThongKeCTActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.btnQLHH:
                Intent intent = new Intent(MainActivity.this, HangHoaActivity.class);
                startActivity(intent);
                return true;
            case R.id.btnSuaNV:
                Intent intent1 = new Intent(MainActivity.this, Xem1NhanVienActivity.class);
                startActivity(intent1);
                return true;
            case R.id.btnQLKH:
                Intent intent2 = new Intent(MainActivity.this, KhachHangActivity.class);
                startActivity(intent2);
                return true;

            case R.id.btnQLCTPDH:
                Intent intent4 = new Intent(MainActivity.this, CT_PhieuDatHangActivity.class);
                startActivity(intent4);
                return true;

            case R.id.btn_QL_PhieuDatHang:
                Intent intent5 = new Intent(MainActivity.this, PhieuDatHangActivity.class);
                startActivity(intent5);
                return true;

            case R.id.btn_QL_PhieuNhapHang:
                Intent intent6 = new Intent(MainActivity.this, PhieuNhapHangActivity.class);
                startActivity(intent6);
                return true;
            case R.id.btnThemPDH:
                Intent intent7= new Intent(MainActivity.this, InsertPhieuDatHangActivity.class);
                startActivity(intent7);
                return true;

            case R.id.btnTK:
                Intent intent8= new Intent(MainActivity.this, ThongKeCTActivity.class);
                startActivity(intent8);
                return true;
            case R.id.btnQLCTPNH:
                Intent intent9 = new Intent(MainActivity.this, CT_PhieuNhapHangActivity.class);
                startActivity(intent9);
                return true;

            case R.id.TimKiemKH:
                Intent intent12 = new Intent(MainActivity.this, TimKiem1KhachHang.class);
                startActivity(intent12);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }