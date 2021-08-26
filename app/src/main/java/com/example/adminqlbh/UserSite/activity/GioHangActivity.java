package com.example.adminqlbh.UserSite.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.LoginActivity;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.GioHang;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.R;
import com.example.adminqlbh.UserSite.adapter.GioHangAdapter;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GioHangActivity extends AppCompatActivity {

    Toolbar toolbarGioHang;
    ListView lvGioHang;
    static TextView textViewTongSoTien;
    ImageView imageViewGiohangTrong;
    TextView textViewThongbaoGiohangTrong;
    Button btnDatHang, btnTiepTucMua;
    GioHangAdapter gioHangAdapter;
    static BigDecimal tongsotien;
    private ApiService apiService;
    private String currentIdPhieuDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_activity_gio_hang);
        migrateComponent();
        actionBarEvent();
        checkGioHang();
        tinhTongTien();
        deleteItemListGioHang();
        buttomEvent();
        Toast.makeText(this, "Số sản phẩm trong giỏ hàng :" + UserHomePageActivity.listGioHang.size(), Toast.LENGTH_SHORT).show();
    }

    private void buttomEvent() {
        btnTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserHomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(UserHomePageActivity.listGioHang.isEmpty()){
                   Toast.makeText(GioHangActivity.this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
               }
               else {
                   insertPhieuDatHang();
               }

            }
        });
    }

    private void insertListCT_PhieuDatHang() {
        apiService = ApiUtils.getApiService();
        ProgressDialog pd = new ProgressDialog(GioHangActivity.this);
        pd.setTitle("Đang tạo đơn hàng....");
        pd.show();
        apiService.getListCT_PhieuDatHang().enqueue(new Callback<List<CT_PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<CT_PhieuDatHang>> call, Response<List<CT_PhieuDatHang>> response) {
                if(response.isSuccessful()){
                    //int nextId = response.body().size();  // Tăng số lượng lên 1 để lấy id tiếp theo
                    // Tách chuỗi PDH*** để lấy số thứ tự ***
                    String[] part = response.body().get(response.body().size()-1).getId().trim().split("H");
                    Integer id = Integer.valueOf(part[1]);
                    int nextId = id + 1;
                    String currentIdCT_PhieuDat = "PDH000";
                    ArrayList<CT_PhieuDatHang> arrCT_PDH = new ArrayList<>();
                    if(id < (999-UserHomePageActivity.listGioHang.size())){
                        for(int i=0; i < UserHomePageActivity.listGioHang.size(); i++){
                            nextId += i;
                            if(nextId < 10){
                                currentIdCT_PhieuDat = "CTDH00" + nextId;
                            }
                            else if(nextId < 100){
                                currentIdCT_PhieuDat = "CTDH0" + nextId;
                            }
                            else currentIdCT_PhieuDat = "CTDH" + nextId;
                            arrCT_PDH.add(new CT_PhieuDatHang(currentIdCT_PhieuDat,
                                    UserHomePageActivity.listGioHang.get(i).getSoluongmua(),
                                    UserHomePageActivity.listGioHang.get(i).getGiaSP().intValue(),
                                    UserHomePageActivity.listGioHang.get(i).getIdSP(),currentIdPhieuDat));
                        }
                    }
                    int arraySize = arrCT_PDH.size();
                    if(arraySize > 0){
                        // Post to server
                       apiService.insertListCT_PDH(arrCT_PDH).enqueue(new Callback<String>() {
                           @Override
                           public void onResponse(Call<String> call, Response<String> response) {
                               if(response.isSuccessful()){
                                   Toast.makeText(GioHangActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                    UserHomePageActivity.listGioHang.clear();
                                    gioHangAdapter.notifyDataSetChanged();
                                    tinhTongTien();
                                    imageViewGiohangTrong.setVisibility(View.VISIBLE);
                                    textViewThongbaoGiohangTrong.setVisibility(View.VISIBLE);
                                    if(UserHomePageActivity.listGioHang.isEmpty()){
                                        btnDatHang.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(GioHangActivity.this, "Giỏ hàng trống" +
                                                        "\nMời bạn tiếp tục mua hàng", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                               }
                               else {
                                   Errors.initListError();
                                   Toast.makeText(GioHangActivity.this, Errors.listErrors.get(response.code()),
                                           Toast.LENGTH_SHORT).show();
                               }
                           }

                           @Override
                           public void onFailure(Call<String> call, Throwable t) {
                               Toast.makeText(GioHangActivity.this, "Lỗi post danh sách CTPDH " + t.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                }
                else {
                    Errors.initListError();
                    Toast.makeText(GioHangActivity.this, Errors.listErrors.get(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CT_PhieuDatHang>> call, Throwable t) {
                Toast.makeText(GioHangActivity.this, "Lỗi lấy danh sách CTPDH " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertPhieuDatHang(){
        apiService = ApiUtils.getApiService();
        apiService.getPhieuDatHang().enqueue(new Callback<List<PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<PhieuDatHang>> call, Response<List<PhieuDatHang>> response) {
                if(response.isSuccessful()){
                    int nextId = response.body().size() + 1;  // Tăng số lượng lên 1 để lấy id tiếp theo
                    if(nextId < 10){
                        currentIdPhieuDat = "PDH00" + nextId;
                    }
                    else if(nextId < 100){
                        currentIdPhieuDat = "PDH0" + nextId;
                    }
                    else currentIdPhieuDat = "PDH" + nextId;
                    Log.d("Thông báo: ", "Mã đơn hàng mới tạo " + currentIdPhieuDat);
                    PhieuDatHang phieuDatHang = new PhieuDatHang();
                    phieuDatHang.setId(currentIdPhieuDat);
                    phieuDatHang.setIdKH(LoginActivity.username.trim());
                    phieuDatHang.setIdNV("NV004");
                    phieuDatHang.setTongTien(tongsotien);
                    phieuDatHang.setTrangThai(0);
                    phieuDatHang.setNgayLap(LocalDate.now().toString());
                    apiService.insertPhieuDatHang(phieuDatHang).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                    try {
                                        insertListCT_PhieuDatHang();
                                    }catch (Exception e){
                                        Toast.makeText(GioHangActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            }else {
                                try {
                                    Errors.initListError();
                                    String errorMessage = Errors.listErrors.get(response.code());
                                    Toast.makeText(GioHangActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    Toast.makeText(GioHangActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(GioHangActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Errors.initListError();
                    Toast.makeText(GioHangActivity.this, Errors.listErrors.get(response.code()),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<PhieuDatHang>> call, Throwable t) {
                Toast.makeText(GioHangActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItemListGioHang() {
        lvGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
                builder.setTitle("Xác nhận xóa sản phẩm");
                builder.setMessage("Bạn có chắc chắn xóa sản phẩm này ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(UserHomePageActivity.listGioHang.isEmpty()){
                            imageViewGiohangTrong.setVisibility(View.VISIBLE);
                            textViewThongbaoGiohangTrong.setVisibility(View.VISIBLE);
                        }
                        else{
                            UserHomePageActivity.listGioHang.remove(position);
                            gioHangAdapter.notifyDataSetChanged();
                            tinhTongTien();
                            if(UserHomePageActivity.listGioHang.isEmpty()){
                                imageViewGiohangTrong.setVisibility(View.VISIBLE);
                                textViewThongbaoGiohangTrong.setVisibility(View.VISIBLE);
                            }
                            else {
                                imageViewGiohangTrong.setVisibility(View.INVISIBLE);
                                textViewThongbaoGiohangTrong.setVisibility(View.INVISIBLE);
                                gioHangAdapter.notifyDataSetChanged();
                                tinhTongTien();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gioHangAdapter.notifyDataSetChanged();
                        tinhTongTien();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void tinhTongTien() {
        BigDecimal tongtien = BigDecimal.ZERO;
        for(int i = 0; i < UserHomePageActivity.listGioHang.size(); i++){
            tongtien = tongtien.add(UserHomePageActivity.listGioHang.get(i).getGiaSP());
        }
        // Format String to vietnamese currency
        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        textViewTongSoTien.setText(formatGia.format(tongtien));
        tongsotien = tongtien;
    }

    // check giỏ hàng trống
    private void checkGioHang() {
        if(UserHomePageActivity.listGioHang.isEmpty()){
            gioHangAdapter.notifyDataSetChanged();
            imageViewGiohangTrong.setVisibility(View.VISIBLE);
            textViewThongbaoGiohangTrong.setVisibility(View.VISIBLE);
            lvGioHang.setVisibility(View.GONE);
            if(UserHomePageActivity.listGioHang.isEmpty()){
                btnDatHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GioHangActivity.this, "Giỏ hàng trống",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else {
            gioHangAdapter.notifyDataSetChanged();
            lvGioHang.setVisibility(View.VISIBLE);
            imageViewGiohangTrong.setVisibility(View.GONE);
            textViewThongbaoGiohangTrong.setVisibility(View.GONE);
        }
    }

    private void migrateComponent() {
        toolbarGioHang = findViewById(R.id.toolbarGioHang);
        lvGioHang = findViewById(R.id.listViewGioHang);
        textViewTongSoTien = findViewById(R.id.textViewTongTien);
        btnDatHang = findViewById(R.id.btnDatHang);
        btnTiepTucMua = findViewById(R.id.btnTiepTucMuaSam);
        imageViewGiohangTrong =findViewById(R.id.imageviewEmptyCart);
        textViewThongbaoGiohangTrong = findViewById(R.id.txtViewThongBaoGioHangTrong);
        setAdapteGioHang(UserHomePageActivity.listGioHang);
    }
    private void setAdapteGioHang(ArrayList<GioHang> list){
        if(gioHangAdapter == null){
            gioHangAdapter = new GioHangAdapter(this, list);
            lvGioHang.setAdapter(gioHangAdapter);
        }
        else {
            gioHangAdapter.notifyDataSetChanged();
        }
    }

    // sự kiện actionBar back click
    private void actionBarEvent(){
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}