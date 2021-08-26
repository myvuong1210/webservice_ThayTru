package com.example.adminqlbh.QuanLyPhieuDatHang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.QuanLyKhachHang.InsertKhachHangActivity;
import com.example.adminqlbh.QuanLyKhachHang.KhachHangActivity;
import com.example.adminqlbh.R;
import com.example.adminqlbh.mailSender.mailSender;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhieuDatHangActivity extends AppCompatActivity {

     public static final String TAG = "Message :";
    LinearLayout linear_listPhieuDatHang, linear_ChiTietPhieuDatHang;
    Button btn_close, btn_update, btn_them;
    TextView textView_maHoaDon, textView_ngayLap, textView_tongTien, textView_khachHang;
    Spinner spinner_trangThai;
    ListView listView_listPhieuDatHang, listView_listChiTietPhieuDatHang;

    List<CT_PhieuDatHang> listCT = new ArrayList<>();
    List<PhieuDatHang> listPhieuDatHang = new ArrayList<>();

    String[] listState = {"Huỷ đơn", "Chờ xác nhận", "Đang giao hàng", "Hoàn thành","Giao hàng thất bại"};
    CT_PhieuDatHangAdapter adapter_CT;
    PhieuDatHang_Adapter _PhieuDatHangAdapter;
    ArrayAdapter<String> spinnerAdapter;
    boolean isCtPhieuDatHangLoaded = false;
    PhieuDatHang current = new PhieuDatHang();
    ApiService apiInterface;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danhsach_phieudathang);
        setControl();
        ActionBar();
        fillListPhieuDathang();

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
        linear_listPhieuDatHang = findViewById(R.id.linear_listPhieuDatHang);
        linear_ChiTietPhieuDatHang = findViewById(R.id.linear_ChiTietPhieuDatHang);
        btn_close = findViewById(R.id.btn_close);
        btn_update = findViewById(R.id.btn_update);
        textView_maHoaDon = findViewById(R.id.textView_maHoaDon);
        textView_ngayLap = findViewById(R.id.textView_ngayLap);
        textView_tongTien = findViewById(R.id.textView_tongTien);
        textView_khachHang = findViewById(R.id.textView_khachHang);
        spinner_trangThai = findViewById(R.id.spinner_trangThai);
        toolbar=findViewById(R.id.toolbar);
        listView_listChiTietPhieuDatHang = findViewById(R.id.listView_listChiTietPhieuDatHang);
        listView_listPhieuDatHang= findViewById(R.id.listView_listPhieuDatHang);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, listState);
        spinner_trangThai.setAdapter(spinnerAdapter);
        listView_listPhieuDatHang.setAdapter(_PhieuDatHangAdapter);
    }
    public void setEvent(){
        listView_listPhieuDatHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                current = listPhieuDatHang.get(position);
                fillListCT(current.getId());
                fillPhieuDatHangINFO(current);
                if(isCtPhieuDatHangLoaded){
                    linear_ChiTietPhieuDatHang.setVisibility(View.VISIBLE);
                    linear_listPhieuDatHang.setVisibility(View.GONE);
                    switch (current.getTrangThai()){

                        case -1:{
                            spinner_trangThai.setAdapter(null);
                            String[] tempList = {"Huỷ đơn"};
                            spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,tempList);
                            spinner_trangThai.setAdapter(spinnerAdapter);
                            break;
                        }

                        case 0:{
                            spinner_trangThai.setAdapter(null);
                            String[] tempList = {"Chờ xác nhận","Hủy đơn", "Đang giao hàng"};
                            spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,tempList);
                            spinner_trangThai.setAdapter(spinnerAdapter);
                            break;
                        }

                        case 1:{
                            spinner_trangThai.setAdapter(null);
                            String[] tempList = {"Đang giao hàng", "Hoàn thành"};
                            spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,tempList);
                            spinner_trangThai.setAdapter(spinnerAdapter);
                            break;
                        }

                        case 2:{
                            spinner_trangThai.setAdapter(null);
                            String[] tempList = {"Hoàn thành"};
                            spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,tempList);
                            spinner_trangThai.setAdapter(spinnerAdapter);
                            break;
                        }

                        case 3:{
                            spinner_trangThai.setAdapter(null);
                            String[] tempList = {"Giao hàng thất bại"};
                            spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,tempList);
                            spinner_trangThai.setAdapter(spinnerAdapter);
                            break;
                        }



                    }
                }
            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_ChiTietPhieuDatHang.setVisibility(View.GONE);
                linear_listPhieuDatHang.setVisibility(View.VISIBLE);

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (spinner_trangThai.getSelectedItem().toString()){
                    case "Huỷ đơn":{
                        current.setTrangThai(-1);
                        sendMail(current.getId());

                        break;
                    }
                    case "Chờ xác nhận":{
                        current.setTrangThai(0);
                        sendMail(current.getId());
                        break;
                    }
                    case "Đang giao hàng":{
                        current.setTrangThai(1);
                        sendMail(current.getId());
                        break;
                    }
                    case "Hoàn thành":{
                        current.setTrangThai(2);
                        sendMail(current.getId());
                        break;
                    }

                    case "Giao hàng thất bại":{
                        current.setTrangThai(3);
                        sendMail(current.getId());
                      break;
                    }
                }
                apiInterface = RetrofitClient.getClient().create(ApiService.class);
                Call<String> call = apiInterface.updatePhieuDatHang(current);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try{
                            String result = response.body();
                            Toast.makeText(PhieuDatHangActivity.this, "Cập nhật đơn hàng thành công", Toast.LENGTH_LONG).show();
                            fillListPhieuDathang();
                            listView_listPhieuDatHang.setAdapter(_PhieuDatHangAdapter);
                            spinner_trangThai.setSelection(current.getTrangThai()+1);
                        } catch (Exception e){
                            Toast.makeText(PhieuDatHangActivity.this, "Can't Fetch CTPhieuDatHang data \nERR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(PhieuDatHangActivity.this, "Can't Fetch CTPhieuDatHang data ", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    // Request api sendMail
    private void sendMail(String idDonHang) {
        apiInterface = ApiUtils.getApiService();
        apiInterface.sendMail(idDonHang).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PhieuDatHangActivity.this, "Mời bạn kiểm tra mail", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PhieuDatHangActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fillListPhieuDathang(){
        apiInterface = RetrofitClient.getClient().create(ApiService.class);
        Call<List<PhieuDatHang>> call = apiInterface.getPhieuDatHang();
        call.enqueue(new Callback<List<PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<PhieuDatHang>> call, Response<List<PhieuDatHang>> response) {
               try{
                   if (response.isSuccessful()){
                       List<PhieuDatHang> listTep = new ArrayList<>();
                       listTep = response.body();
                       listPhieuDatHang.clear();
                       for(PhieuDatHang temp : listTep){
                           listPhieuDatHang.add(temp);
                       }
                       _PhieuDatHangAdapter = new PhieuDatHang_Adapter(listPhieuDatHang);
                       _PhieuDatHangAdapter.notifyDataSetChanged();
                           setControl();
                           setEvent();
                   }
               } catch (Exception e){
                   Toast.makeText(PhieuDatHangActivity.this, "Can't Fetch PhieuDatHang data \nERR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onFailure(Call<List<PhieuDatHang>> call, Throwable t) {
                Toast.makeText(PhieuDatHangActivity.this, "Can't get PhieuDatHang data", Toast.LENGTH_LONG).show();
            }
        });
   }
   public void fillListCT(String maPhieu){
        apiInterface = RetrofitClient.getClient().create(ApiService.class);
        Call<List<CT_PhieuDatHang>> call = apiInterface.getCT(maPhieu);
        call.enqueue(new Callback<List<CT_PhieuDatHang>>() {
            @Override
            public void onResponse(Call<List<CT_PhieuDatHang>> call, Response<List<CT_PhieuDatHang>> response) {
                try{
                    isCtPhieuDatHangLoaded = false;
                    if(response.isSuccessful()){
                        List<CT_PhieuDatHang> listTemp = new ArrayList<>();
                        listTemp = response.body();
                        listCT.clear();
                        for(CT_PhieuDatHang temp: listTemp){
                            listCT.add(temp);
                        }
                        adapter_CT = new CT_PhieuDatHangAdapter(listCT);
                        adapter_CT.notifyDataSetChanged();
                        listView_listChiTietPhieuDatHang.setAdapter(adapter_CT);
                        isCtPhieuDatHangLoaded = true;
                    }
                } catch (Exception e){
                    Toast.makeText(PhieuDatHangActivity.this, "Can't Fetch CTPhieuDatHang data \nERR: "+ e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<CT_PhieuDatHang>> call, Throwable t) {
                Toast.makeText(PhieuDatHangActivity.this, "Can't get CTPhieuDatHang data", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });

   }

    Format format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    public void fillPhieuDatHangINFO(PhieuDatHang temp){
        String str[] = temp.getNgayLap().split("-");
        int nam = Integer.parseInt(str[0]);
        int thang = Integer.parseInt(str[1]);
        int ngay=Integer.parseInt(str[2]);
        textView_khachHang.setText("Mã Khách hàng: "+temp.getIdKH());
        textView_tongTien.setText("Tổng tiền: "+format.format(temp.getTongTien()));
        textView_ngayLap.setText("Ngày lập: " +ngay+"/"+thang+"/"+nam);
        textView_maHoaDon.setText("Mã hóa đơn: " +temp.getId());
        spinner_trangThai.setSelection(temp.getTrangThai()+1);
   }


}