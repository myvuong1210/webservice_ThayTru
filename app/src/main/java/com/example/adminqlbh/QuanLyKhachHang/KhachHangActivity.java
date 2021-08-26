
package com.example.adminqlbh.QuanLyKhachHang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity;
import com.example.adminqlbh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhachHangActivity extends AppCompatActivity {

    public static final String EXTRA_KHACHHANG = "extra_khachhang";
    public static final String TAG = "Message :";
    public static List<KhachHang> tempListKH = new ArrayList<>();
    public static int curentPositionKH;

    private SwipeMenuListView lvKhachHang;
    private FloatingActionButton btnThemKH;
    private KhachHangAdapter adapter;
    private List<KhachHang> listKH;

    private boolean isDeleteSuccess = false;
    private ApiService apiService;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danhsach_khachhang);
        setControl();
        ActionBar();
        setEvent();
        if (listKH == null) {
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
        tempListKH.clear();
        super.onDestroy();
    }

    // refresh adapter khi data trong list view thay đổi
    // sau khi activity onStop()
    @Override
    protected void onStart() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            lvKhachHang.setSelection(adapter.getCount() - 1);
        }
        super.onStart();
    }

    public void setControl() {
        lvKhachHang = (SwipeMenuListView) findViewById(R.id.lvListKH);
        btnThemKH = (FloatingActionButton) findViewById(R.id.addKH);
        toolbar =findViewById(R.id.toolbar);
    }


    public void setAdapterListKH(List<KhachHang> lstKH) {
        if (adapter == null) {
            adapter = new KhachHangAdapter(this, R.layout.layout_item_khachhang, lstKH);
            lvKhachHang.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
            lvKhachHang.setSelection(adapter.getCount() - 1);
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
        lvKhachHang.setMenuCreator(creator);
    }

    public void setEvent() {
        // Button them click
        btnThemKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                // Move to InsertKhachHangActivity
                Intent intent = new Intent(KhachHangActivity.this, InsertKhachHangActivity.class);
                startActivity(intent);
            }
        });
        // list hàng hóa - swipe event
        lvKhachHang.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                KhachHang khachHang = tempListKH.get(position);
                switch (index) {
                    // update
                    case 0:
                        curentPositionKH = position;
                        updateKhachHangActivity(khachHang);
                        break;
                    // delete
                    case 1:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(KhachHangActivity.this);
                        //set icon
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        //set title
                        alertDialog.setTitle("Cảnh báo");
                        //set message
                        alertDialog.setMessage("Bạn chắc chắn muốn xóa ?" + "\n" + khachHang.getHoTen());
                        //set positive button
                        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                curentPositionKH = position;
                                deleteKhachHang(khachHang.getId());
                                Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                ApiUtils.delay(3, new ApiUtils.DelayCallback() {
                                    @Override
                                    public void afterDelay() {
                                        if(isDeleteSuccess){
                                            Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                            tempListKH.remove(position);
                                            adapter.notifyDataSetChanged();
                                            isDeleteSuccess = false;
                                        }
                                    }
                                });
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

    public void updateKhachHangActivity(KhachHang hh) {
        Intent intent = new Intent(KhachHangActivity.this, UpdateKhachHangActivity.class);
        //package data KhachHang to send updateKhachHangActivity
        // using Bundle class
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_KHACHHANG, hh);

        //send
        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void loadData() {
        listKH = new ArrayList<>();
        apiService = ApiUtils.getApiService();
        apiService.getListKhachHang().enqueue(new Callback<List<KhachHang>>() {
            @Override
            public void onResponse(Call<List<KhachHang>> call, Response<List<KhachHang>> response) {
                try {
                    if (response.isSuccessful()) {
                        listKH = response.body();
                        // using temp list to store data
                        tempListKH.addAll(listKH);
                        setAdapterListKH(tempListKH);
                    } else {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(),
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: " + e);
                }
            }

            @Override
            public void onFailure(Call<List<KhachHang>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteKhachHang(String id) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.deleteKhachHang(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công !",
                            Toast.LENGTH_SHORT).show();
                    tempListKH.remove(curentPositionKH);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
                if (response.code() == 406) {
                    Toast.makeText(getApplicationContext(), "Không thể xóa !" + "\nDính khóa ngoại!",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Mã lỗi: " + response.body());
                    pd.dismiss();
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), "ID not found !",
                            Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), "Lỗi server !",
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
}
