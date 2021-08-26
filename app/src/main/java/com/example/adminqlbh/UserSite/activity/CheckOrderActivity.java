package com.example.adminqlbh.UserSite.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.LoginActivity;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.QuanLyPhieuDatHang.PhieuDatHang_Adapter;
import com.example.adminqlbh.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOrderActivity extends AppCompatActivity {

    private ApiService apiService;
    private Toolbar toolbarCheckOrder;
    private SwipeMenuListView listview_DonHang;
    private TextView tvDonHang_TenKH;
    private List<PhieuDatHang> listDonHang;
    private PhieuDatHang_Adapter donHangAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_activity_kiemtra_donhang);
        migrateComponent();
        actionBar();
        tvDonHang_TenKH.setText("Wellcome "+LoginActivity.tenKhachHang);
        getListDonHangByIDKH();
        listViewEventItem();
    }

    private void listViewEventItem() {
        listview_DonHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckOrderActivity.this, ChiTietDonHangActivity.class);
                intent.putExtra("ID DON DAT HANG", listDonHang.get(position).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        // list đơn hàng - swipe event
        listview_DonHang.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                PhieuDatHang phieuDatHang = listDonHang.get(position);
                switch (index) {
                    // Hủy đơn
                    case 0:
                        // -1 : Hủy đơn. 0 : Chờ xác nhận, 1 : Đang giao hàng, 2 : Hoàn thành
                        if(phieuDatHang.getTrangThai() == -1){
                            Toast.makeText(CheckOrderActivity.this, "Đơn đã bị hủy", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(phieuDatHang.getTrangThai() == 1){
                            Toast.makeText(CheckOrderActivity.this, "Đơn hàng đang được giao"
                                    + "\nKhông thể hủy đơn", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(phieuDatHang.getTrangThai() == 2){
                            Toast.makeText(CheckOrderActivity.this, "Đơn đã hoàn thành"
                                    + "\nKhông thể hủy đơn", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(phieuDatHang.getTrangThai() == 0){
                            // show Alert dialog when delete hang hoa
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckOrderActivity.this);
                            //set icon
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            //set title
                            alertDialog.setTitle("Xác nhận hủy đơn");
                            //set message
                            // convert ngày lập sang LocalDate;
                            LocalDate localDate = LocalDate.parse(listDonHang.get(position).getNgayLap(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            // format ngày "dd/MM/yyyy"
                            DateTimeFormatter fomartter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String ngaylap = localDate.format(fomartter);
                            alertDialog.setMessage("Bạn chắc chắn muốn hủy đơn hàng vào ngày " + ngaylap + " ?");
                            //set positive button
                            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    phieuDatHang.setTrangThai(-1);
                                    apiService = ApiUtils.getApiService();
                                    apiService.updatePhieuDatHang(phieuDatHang).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if(response.isSuccessful()){
                                                Toast.makeText(CheckOrderActivity.this, "HỦy thành công", Toast.LENGTH_SHORT).show();
                                                donHangAdapter.notifyDataSetChanged();
                                            }
                                            else {
                                                Errors.initListError();
                                                Toast.makeText(CheckOrderActivity.this,
                                                        Errors.listErrors.get(response.code()), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(CheckOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            //set negative button
                            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialog.show();
                            break;
                        }
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    private void getListDonHangByIDKH() {
        apiService = ApiUtils.getApiService();
        apiService.getListPDHByID(LoginActivity.username).enqueue(new Callback<List<PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<PhieuDatHang>> call, Response<List<PhieuDatHang>> response) {
                if (response.isSuccessful()){
                    listDonHang = response.body();
                    if(listDonHang.isEmpty()){
                        Toast.makeText(CheckOrderActivity.this, "Đơn hàng chưa có sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    else setAdapterListDonHang(listDonHang);
                }
                else if(response.body().isEmpty()){

                }
                else {
                    try{
                        String errorMessage = Errors.listErrors.get(response.code());
                        Toast.makeText(CheckOrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(CheckOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PhieuDatHang>> call, Throwable t) {
                Toast.makeText(CheckOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void migrateComponent() {
        toolbarCheckOrder = findViewById(R.id.toolbarCheckOrder);
        listview_DonHang = findViewById(R.id.lvCheckOrder);
        tvDonHang_TenKH = findViewById(R.id.tvCheckOrderTenKH);
    }

    // sự kiện actionBar back click
    private void actionBar(){
        setSupportActionBar(toolbarCheckOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarCheckOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setAdapterListDonHang(List<PhieuDatHang> listDonHang){
        if(donHangAdapter == null){
            // set adapter san pham
            donHangAdapter = new PhieuDatHang_Adapter(listDonHang);
            listview_DonHang.setAdapter(donHangAdapter);
        }
        else {
            donHangAdapter.notifyDataSetChanged();
        }
        // Create SwipeMenu item
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Hủy đơn");
                // set item title fontsize
//                openItem.setIcon(R.drawable.ic_cancel_bill);
                openItem.setTitleSize(15);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        // set creator
        listview_DonHang.setMenuCreator(creator);
    }


}