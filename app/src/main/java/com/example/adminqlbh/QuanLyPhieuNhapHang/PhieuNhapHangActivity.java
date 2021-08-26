
package com.example.adminqlbh.QuanLyPhieuNhapHang;

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
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.Models.PhieuNhapHang;
import com.example.adminqlbh.QuanLyCT_PhieuNhapHang.CT_PhieuNhapHangAdapter;
import com.example.adminqlbh.QuanLyPhieuNhapHang.InsertPhieuNhapHangActivity;
import com.example.adminqlbh.QuanLyPhieuNhapHang.UpdatePhieuNhapHangActivity;
import com.example.adminqlbh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhieuNhapHangActivity extends AppCompatActivity {

    public static final String EXTRA_PNH = "extra_pnh";
    public static final String TAG = "Message :";
    public static List<PhieuNhapHang> tempListPNH = new ArrayList<>();
    public static int curentPositionPNH;


    private SwipeMenuListView lvPhieuNhapHang;
    private FloatingActionButton btnThemPNH;
    private List<PhieuNhapHang> listPNH;

    private boolean isDeleteSuccess = false;
    private ApiService apiService;
    private PhieuNhapHangAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danhsach_phieunhaphang);
        setControl();
        ActionBar();
        setEvent();
        if(listPNH == null){
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


    // Clear ds hàng hóa tạm khi activity bị onDestroy()
    // sau khi ấn phím back
    @Override
    protected void onDestroy() {
        tempListPNH.clear();
        super.onDestroy();
    }

    // refresh adapter khi data trong list view thay đổi
    // sau khi activity onStop()
    @Override
    protected void onStart() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
            lvPhieuNhapHang.setSelection(adapter.getCount()-1);
        }
        super.onStart();
    }

    public void setControl(){
        lvPhieuNhapHang = (SwipeMenuListView)findViewById(R.id.lvListPNH);
        btnThemPNH = (FloatingActionButton)findViewById(R.id.addPNH);
        toolbar=findViewById(R.id.toolbar);
    }


    public void setAdapterListPNH(List<PhieuNhapHang> lstPNH){
        if (adapter == null) {
            adapter = new PhieuNhapHangAdapter(this, R.layout.layout_item_chitietnhaphang,lstPNH);
            lvPhieuNhapHang.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
            lvPhieuNhapHang.setSelection(adapter.getCount()-1);
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0x41, 0xcd,
                        0xf0)));
                openItem.setWidth(170);
                openItem.setIcon(R.drawable.ic_edit);
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        lvPhieuNhapHang.setMenuCreator(creator);
    }

    public void setEvent(){
        btnThemPNH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(PhieuNhapHangActivity.this, InsertPhieuNhapHangActivity.class);
                startActivity(intent);
            }
        });
        lvPhieuNhapHang.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                PhieuNhapHang PhieuNhapHang = tempListPNH.get(position);
                switch (index) {
                    case 0:
                        curentPositionPNH = position;
                        updatePNHActivity(PhieuNhapHang);
                        break;
                    case 1:
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhieuNhapHangActivity.this);
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialog.setTitle("Cảnh báo");
                        alertDialog.setMessage("Bạn chắc chắn muốn xóa ?" + "\n");
                        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                curentPositionPNH = position;
                                deletePNH(PhieuNhapHang.getId());
                                Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                ApiUtils.delay(3, new ApiUtils.DelayCallback() {
                                    @Override
                                    public void afterDelay() {
                                        if(isDeleteSuccess){
                                            Log.d(TAG, "isDeleteSucess : " + isDeleteSuccess);
                                            tempListPNH.remove(position);
                                            adapter.notifyDataSetChanged();
                                            isDeleteSuccess = false;
                                        }
                                    }
                                });
                            }
                        });
                        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();

                        break;
                }
                return false;
            }
        });
    }


    public void updatePNHActivity(PhieuNhapHang ct) {
        Intent intent = new Intent(PhieuNhapHangActivity.this, UpdatePhieuNhapHangActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PNH, ct);

        //send
        intent.putExtras(bundle);

        startActivity(intent);
    }
    public void loadData(){
        listPNH = new ArrayList<>();
        apiService = ApiUtils.getApiService();
        apiService.getListPhieuNhapHang().enqueue(new Callback<List<PhieuNhapHang>>() {
            @Override
            public void onResponse(Call<List<PhieuNhapHang>> call, Response<List<PhieuNhapHang>> response) {
                try {
                    if(response.isSuccessful()){
                        listPNH = response.body();
                        tempListPNH.addAll(listPNH);
                        setAdapterListPNH(tempListPNH);
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
            public void onFailure(Call<List<PhieuNhapHang>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deletePNH(String id) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.deletePNH(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công !",
                            Toast.LENGTH_SHORT).show();
                    tempListPNH.remove(curentPositionPNH);
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
