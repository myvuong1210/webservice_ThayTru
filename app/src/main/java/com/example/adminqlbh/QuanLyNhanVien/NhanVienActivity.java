
package com.example.adminqlbh.QuanLyNhanVien;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.LoginActivity;
import com.example.adminqlbh.MainActivity;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NhanVienActivity extends AppCompatActivity {

    public static final String EXTRA_NhanVien = "extra_nhanvien";
    public static final String TAG = "Message :";
    public static List<NhanVien> tempListNV = new ArrayList<>();
    public static int curentPositionNV;

    private SwipeMenuListView lvNhanVien;
    private FloatingActionButton btnThemNV;
    private NhanVienAdapter adapter;
    private List<NhanVien> listNV;

    private boolean isDeleteSuccess = false;

    private ApiService apiService;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danhsach_nhanvien);
        setControl();
        ActionBar();
        setEvent();
        if(listNV == null){
            loadData();
        }
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

    @Override
    protected void onDestroy() {
        tempListNV.clear();
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
            lvNhanVien.setSelection(adapter.getCount()-1);
        }
        super.onStart();
    }

    public void setControl(){
        lvNhanVien = (SwipeMenuListView)findViewById(R.id.lvListNV);
        btnThemNV = (FloatingActionButton)findViewById(R.id.addNV);
        toolbar=findViewById(R.id.toolbar);
    }


    public void setAdapterListNV(List<NhanVien> lstNV){
        if (adapter == null) {
            adapter = new NhanVienAdapter(this, R.layout.layout_item_nhanvien,lstNV);
            lvNhanVien.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
            lvNhanVien.setSelection(adapter.getCount()-1);
        }

        // Create SwipeMenu item
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x41, 0xcd,
                        0xf0)));
                // set item width
                openItem.setWidth(170);
                // set item title
                //openItem.setTitle("Sửa");
                // set item title fontsize
                openItem.setIcon(R.drawable.ic_edit);
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lvNhanVien.setMenuCreator(creator);
    }

    public void setEvent(){
        // Button them click
        btnThemNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                // Move to InsertNhanVienActivity
                Intent intent = new Intent(NhanVienActivity.this, InsertNhanVienActivity.class);
                startActivity(intent);
            }
        });
        // list hàng hóa - swipe event
        lvNhanVien.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                NhanVien nhanVien = tempListNV.get(position);
                switch (index) {
                    // update
                    case 0:
                        curentPositionNV = position;
                        updateNhanVienActivity(nhanVien);
                        break;
                    // delete

                    case 1:
                            // show Alert dialog when delete hang hoa
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NhanVienActivity.this);
                            //set icon
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            //set title
                            alertDialog.setTitle("Cảnh báo");
                            //set message
                            alertDialog.setMessage("Bạn chắc chắn muốn xóa ?");
                            //set positive button
                            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    curentPositionNV = position;
                                    if (!(listNV.get(position).getId().equalsIgnoreCase(LoginActivity.username))) {
                                        deleteNhanVien(nhanVien.getId());
                                        Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                        ApiUtils.delay(1, new ApiUtils.DelayCallback() {
                                            @Override
                                            public void afterDelay() {
                                                if (isDeleteSuccess) {
                                                    Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                                    tempListNV.remove(position);
                                                    adapter.notifyDataSetChanged();
                                                    isDeleteSuccess = false;
                                                }
                                            }
                                        });

                                    }

                                }



                            });
                            //set negative button
                            alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            alertDialog.show();

                            break;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }


    public void updateNhanVienActivity(NhanVien hh) {
        Intent intent = new Intent(NhanVienActivity.this, UpdateNhanVienActivity.class);
        //package data NhanVien to send updateNhanVienActivity
        // using Bundle class
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_NhanVien, hh);

        //send
        intent.putExtras(bundle);

        startActivity(intent);
    }
    public void loadData(){
        listNV = new ArrayList<>();
        apiService = ApiUtils.getApiService();
        apiService.getListNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                try {
                    if(response.isSuccessful()){
                        listNV = response.body();
                        // using temp list to store data
                        tempListNV.addAll(listNV);
                        setAdapterListNV(tempListNV);
                    }else {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: " + e);
                }
            }
            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void deleteNhanVien(String id){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.deleteNhanVien(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Xóa thành công !",
                            Toast.LENGTH_SHORT).show();

                    tempListNV.remove(curentPositionNV);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
                if(response.code() == 406){
                    Toast.makeText(getApplicationContext(),"Không thể xóa !" + "\nDính khóa ngoại!",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Mã lỗi: " + response.body());
                    pd.dismiss();
                }
                else if(response.code() == 400){
                    Toast.makeText(getApplicationContext(),"ID not found !",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else if(response.code() == 500) {
                    Toast.makeText(getApplicationContext(),"Lỗi server !",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_timnv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.TimKiemNV:
                Intent intent = new Intent(NhanVienActivity.this, TimKiem1NhanVien.class);
                startActivity(intent);
                return true;
        }
            return super.onOptionsItemSelected(item);
        }
    }
