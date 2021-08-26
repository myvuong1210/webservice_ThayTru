package com.example.adminqlbh.UserSite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.LoginActivity;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.GioHang;
import com.example.adminqlbh.Models.HangHoaFull;
import com.example.adminqlbh.R;
import com.example.adminqlbh.UserSite.adapter.SanPhamAdapter;
import com.example.adminqlbh.Util.InternetConnnection;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomePageActivity extends AppCompatActivity{

    androidx.appcompat.widget.Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewProctHomePage;
    NavigationView navigationView;
    ListView listViewHomePage;
    DrawerLayout drawerLayoutHomePage;
    TextView tvTenKH;
    ApiService apiService;
    List<HangHoaFull> listHHFull;
    SanPhamAdapter sanPhamAdapter;
    public static ArrayList<GioHang> listGioHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_home_page);
        Errors.initListError();
        migrateWidget();
        ActionBar();
        ActionViewFlipper();
        itemNavigationViewClick();
        getFullHangHoa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_giohang, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGiohang:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void itemNavigationViewClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Toast.makeText(UserHomePageActivity.this, "Bạn đang ở trang chủ", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_info:
                        Toast.makeText(UserHomePageActivity.this, "Xin chào " + LoginActivity.tenKhachHang, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_contact:
                        Toast.makeText(UserHomePageActivity.this, "n17dccn138@student.ptithcm.edu.vn", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_checkOrder:
                        if(InternetConnnection.haveNetworkConnection(UserHomePageActivity.this)){
                            Intent intent = new Intent(UserHomePageActivity.this, CheckOrderActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.navigation_logout:
                        listGioHang.clear();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    public void setAdapterListHangHoaFull(List<HangHoaFull> listSanPham){
        if(sanPhamAdapter == null){
            // set adapter san pham
            sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), listSanPham);
            recyclerViewProctHomePage.setHasFixedSize(true);
            recyclerViewProctHomePage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            recyclerViewProctHomePage.setAdapter(sanPhamAdapter);
        }
        else {
            sanPhamAdapter.notifyDataSetChanged();
        }
    }

    private void getFullHangHoa() {
        apiService = ApiUtils.getApiService();
        apiService.geHangHoaFull().enqueue(new Callback<List<HangHoaFull>>() {
            @Override
            public void onResponse(Call<List<HangHoaFull>> call, Response<List<HangHoaFull>> response) {
                if(response.isSuccessful()){
                    listHHFull = response.body();
                    setAdapterListHangHoaFull(listHHFull);
                }
                else {
                    try{
                        String errorMessage = Errors.listErrors.get(response.code());
                        Toast.makeText(UserHomePageActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(UserHomePageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<HangHoaFull>> call, Throwable t) {
                Toast.makeText(UserHomePageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ActionViewFlipper() {
        ArrayList<Integer> bannerList = new ArrayList<>();
        bannerList.add(R.drawable.banner2);
        bannerList.add(R.drawable.banner3);
        bannerList.add(R.drawable.banner4);
        bannerList.add(R.drawable.banner_monitor);
        bannerList.add(R.drawable.amd_banner);

        for(int i = 0; i < bannerList.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
//            Picasso.get().load(bannerList.get(i)).into(imageView);
            imageView.setImageResource(bannerList.get(i));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayoutHomePage.openDrawer(GravityCompat.START);
            }
        });
    }

    private void migrateWidget() {
        toolbar = findViewById(R.id.toolbarHomePage);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerViewProctHomePage = findViewById(R.id.recyclerViewProctHomePage);
        navigationView = findViewById(R.id.navigationView);
        listViewHomePage = findViewById(R.id.listViewHomePage);
        drawerLayoutHomePage = findViewById(R.id.drawerLayoutHomePage);
        tvTenKH = findViewById(R.id.tvHeaderNavLogiNname);
        if(listGioHang != null){

        }else {
            listGioHang = new ArrayList<>();
        }
    }


}