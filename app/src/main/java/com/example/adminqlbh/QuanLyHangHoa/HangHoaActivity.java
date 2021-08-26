
package com.example.adminqlbh.QuanLyHangHoa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HangHoaActivity extends AppCompatActivity {

    public static final String EXTRA_HANGHOA = "extra_hanghoa";
    public static final String TAG = "Message :";
    public static List<HangHoa> tempListHH = new ArrayList<>();
    public static int curentPosition;

    private SwipeMenuListView lvHangHoa;
    private FloatingActionButton btnThemHH;
    private HangHoaAdapter adapter;
    private List<HangHoa> listHH;
    private androidx.appcompat.widget.Toolbar toolbarHangHoa;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_danhsach_hanghoa);
        setControl();
        setEvent();
        ActionBar();
        Errors.initListError();
        if(listHH == null){
            loadData();
        }

    }

    private void ActionBar() {
        setSupportActionBar(toolbarHangHoa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHangHoa.setNavigationOnClickListener(new View.OnClickListener() {
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
        tempListHH.clear();
        super.onDestroy();
    }

    // refresh adapter khi data trong list view thay đổi
    // sau khi activity onStop()
    @Override
    protected void onStart() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
            lvHangHoa.setSelection(adapter.getCount()-1);
        }
        super.onStart();
    }

    public void setControl(){
        toolbarHangHoa = findViewById(R.id.toolbarQLHH);
        lvHangHoa = (SwipeMenuListView)findViewById(R.id.lvListHH);
        btnThemHH = (FloatingActionButton)findViewById(R.id.fab_addProct);
    }


    public void setAdapterListHH(List<HangHoa> lstHH){
        if (adapter == null) {
            adapter = new HangHoaAdapter(this, R.layout.layout_item_hanghoa,lstHH);
            lvHangHoa.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
            lvHangHoa.setSelection(adapter.getCount()-1);
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
        lvHangHoa.setMenuCreator(creator);
    }

    public void setEvent(){
        // Button them click
        btnThemHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                // Move to InsertHangHoaActivity
                Intent intent = new Intent(HangHoaActivity.this, InsertHangHoaActivity.class);
                startActivity(intent);
            }
        });
        // list hàng hóa - swipe event
        lvHangHoa.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                HangHoa hangHoa = tempListHH.get(position);
                switch (index) {
                    // update
                    case 0:
                        curentPosition = position;
                        updateProctActivity(hangHoa);
                        break;
                    // delete
                    case 1:
                        // show Alert dialog when delete hang hoa
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HangHoaActivity.this);
                        //set icon
                        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                        //set title
                        alertDialog.setTitle("Cảnh báo");
                        //set message
                        alertDialog.setMessage("Bạn chắc chắn muốn xóa ?" + "\n" + hangHoa.getTen());
                        //set positive button
                        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        curentPosition = position;
                                        deleteHangHoa(hangHoa.getId());
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

    public void updateProctActivity(HangHoa hh) {
        Intent intent = new Intent(HangHoaActivity.this, UpdateHangHoaActivity.class);
        //package data hanghoa to send updateHangHoaActivity
        // using Bundle class
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_HANGHOA, hh);

        //send
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //====================Get list Hang hoa====================
    public void loadData(){
        listHH = new ArrayList<>();
        apiService = ApiUtils.getApiService();
        apiService.getListProduct().enqueue(new Callback<List<HangHoa>>() {
            @Override
            public void onResponse(Call<List<HangHoa>> call, Response<List<HangHoa>> response) {
                try {
                    if(response.isSuccessful()){
                        listHH = response.body();
                        // using temp list to store data
                        tempListHH.addAll(listHH);
                        setAdapterListHH(tempListHH);
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
            public void onFailure(Call<List<HangHoa>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //===================Delete hang hoa======================
    public void deleteHangHoa(String id){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.deleteHangHoa(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Xóa thành công !",
                                    Toast.LENGTH_SHORT).show();
                    // Sau khi server response xóa thành công
                    // Xóa hàng hóa trong danh danh sách tạm
                    tempListHH.remove(curentPosition);
                    adapter.notifyDataSetChanged();
                    pd.dismiss();
                }
                else {
                    try {
                        String errorMessage = Errors.listErrors.get(response.code());
                        Toast.makeText(HangHoaActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(HangHoaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
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
