
package com.example.adminqlbh.QuanLyHangHoa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.adminqlbh.Api.ApiService;
import com.example.adminqlbh.Api.ApiUtils;
import com.example.adminqlbh.Models.CT_GiaNhap;
import com.example.adminqlbh.Models.CT_GiaNiemYet;
import com.example.adminqlbh.Models.Errors;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.R;
import com.example.adminqlbh.Util.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.TAG;
import static com.example.adminqlbh.QuanLyHangHoa.HangHoaActivity.tempListHH;

public class InsertHangHoaActivity extends AppCompatActivity {

    public final static int PICK_IMAGE_REQUEST = 1;
    public final static int READ_EXTERNAL_REQUEST = 2;
    private EditText txtInputId, txtInputTenHH,
            txtInputMota, txtInputKL, txtInputGiaNiemYet, txtGiaNhap;
    private Button btnAddHH;
    private ImageView imageView_Proct;
    private Uri imageUriGallery;
    private String imageFileName;
    private ApiService apiService;
    private String idHH;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insert_activity_hanghoa);

        setControl();
        createIDHangHoa();
        setEvent();
    }

    private void createIDHangHoa() {
        apiService = ApiUtils.getApiService();
        apiService.getListProduct().enqueue(new Callback<List<HangHoa>>() {
            @Override
            public void onResponse(Call<List<HangHoa>> call, Response<List<HangHoa>> response) {
                if(response.isSuccessful()){
                    if(response.body().isEmpty()){
                        idHH = "HH001";
                    }
                    else {
                        int nextID = 0;
                        // Tách chuỗi để lấy số thứ tự ***
                        String[] part = response.body().get(response.body().size()-1).getId().trim().split("H");
                        Integer id = Integer.valueOf(part[2]);
                        nextID = id + 1;
                        if(nextID < 10){
                             idHH = "HH00" + nextID;
                        }
                        else if(nextID < 100){
                            idHH = "HH0" + nextID;
                        }
                        else idHH =  "HH" + nextID;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<HangHoa>> call, Throwable t) {

            }
        });
    }

    private void setControl(){
        imageView_Proct = findViewById(R.id.iv_addImgBTV);
        txtInputId = findViewById(R.id.txtInputId);
        txtInputId.setEnabled(false);
        txtInputTenHH = findViewById(R.id.txtInputTenHH);
        txtInputMota = findViewById(R.id.txtInputMota);
        txtInputKL = findViewById(R.id.txtInputKL);
        txtInputGiaNiemYet =findViewById(R.id.txtInputGIA);
        txtGiaNhap =findViewById(R.id.txtInputGiaNhap);
        btnAddHH = findViewById(R.id.btnThemHH);
        btnAddHH.setText("THÊM");
    }



    //====================Validate======================
    public boolean isValidInput(){
        if(imageView_Proct.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "Bạn chưa chọn ảnh !!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ApiUtils.isEmpty(txtInputTenHH)){
            txtInputTenHH.setError("Tên hàng hóa không được để trống !");
            return false;
        }
        if(ApiUtils.isEmpty(txtInputMota)){
            txtInputMota.setError("Mô tả hàng hóa không được để trống !");
            return false;
        }
        if(ApiUtils.isEmpty(txtInputKL)){
            txtInputKL.setError("Khối lượng hàng hóa không được để trống "
                    + "\n" + "và phải nhỏ hơn 10 kí tự !");
            return false;
        }
        return true;
    }

    // Get hàng hóa
    private HangHoa getHangHoa(){
        HangHoa hh = new HangHoa();
        hh.setId(idHH);
        hh.setTen(txtInputTenHH.getText().toString().trim());
        hh.setMoTa(txtInputMota.getText().toString().trim());
        hh.setAnh(imageFileName);
        hh.setSoluongTon(0);
        hh.setKhoiLuong(txtInputKL.getText().toString().trim());
        return hh;
    }
    // Get giá niêm yết
    private CT_GiaNiemYet getGiaNiemYet(){
        CT_GiaNiemYet ct_giaNiemYet = new CT_GiaNiemYet();
        ct_giaNiemYet.setNgayapdung(LocalDate.now().toString());
        ct_giaNiemYet.setGia(new BigDecimal(txtInputGiaNiemYet.getText().toString()));
        ct_giaNiemYet.setIdHH(idHH);
        return ct_giaNiemYet;
    }
    // Get giá nhập
    private CT_GiaNhap getGiaNhap(){
        CT_GiaNhap ct_giaNhap = new CT_GiaNhap();
        ct_giaNhap.setNgayapdung(LocalDate.now().toString());
        ct_giaNhap.setGia(new BigDecimal(txtGiaNhap.getText().toString()));
        ct_giaNhap.setIdHH(idHH);
        return ct_giaNhap;
    }

    //===================Insert hang hoa======================
    private void insertHangHoa(){
        apiService = ApiUtils.getApiService();
        apiService.postHangHoa(getHangHoa()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Log.d("Response body update", "onResponse: " + response.body());
                    ThemCT_GiaNiemYet();
                }
                if(response.code() == 400){
                    Toast.makeText( getApplicationContext(),"ID đã tồn tại !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: ID đã tồn tại !" + response.body());

                }
                if(response.code() == 500){
                    Toast.makeText( getApplicationContext(),"Lỗi server !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Lỗi ", "onResponse: Lỗi server !" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Thêm hàng hóa ", "onFailure: " + t.getMessage());
            }
        });
    }

    //=========================Insert Gia niêm yết======================
    public void ThemCT_GiaNiemYet(){

        apiService = ApiUtils.getApiService();
        apiService.createGiaNiemYet(getGiaNiemYet()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Thêm giá niêm yết thành công ", "onResponse: " + getGiaNiemYet().getNgayapdung());

                    // Sau khi server response thêm giá niêm yết ngày hiện tại thành công
                    // Thêm mới danh sách tạm và thoát activity
                    ThemCT_GiaNNhap();
                }
                if(response.code() == 400){
                    deleteHangHoaByID();
                    Toast.makeText(getApplicationContext(), "HÀNG HÓA ĐÃ CÓ GIÁ NGÀY HÔM NAY", Toast.LENGTH_SHORT).show();
                    Log.d("Thêm giá niêm yết thất bại ", "onResponse: HÀNG HÓA ĐÃ CÓ GIÁ NGÀY HÔM NAY"
                            + getGiaNiemYet().getNgayapdung());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Thêm giá niêm yết ", "onFailure: " + t.getMessage());
            }
        });
    }

    //=========================Insert giá nhập======================
    public void ThemCT_GiaNNhap(){
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Đang xử lý....");
        pd.show();
        apiService = ApiUtils.getApiService();
        apiService.createGiaNhap(getGiaNhap()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText( getApplicationContext(),"Thêm thành công !",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Thêm giá nhập thành công ", "onResponse: " + getGiaNiemYet().getNgayapdung());
                    // Sau khi server response thêm giá niêm yết ngày hiện tại thành công
                    // Thêm mới danh sách tạm và thoát activity
                    tempListHH.add(getHangHoa());
                    pd.dismiss();
                    finish();
                }
                if(response.code() == 400){
                    deleteHangHoaByID();
                    Toast.makeText(getApplicationContext(), "HÀNG HÓA ĐÃ CÓ GIÁ NGÀY HÔM NAY", Toast.LENGTH_SHORT).show();
                    Log.d("Thêm giá niêm yết thất bại ", "onResponse: HÀNG HÓA ĐÃ CÓ GIÁ NGÀY HÔM NAY"
                            + getGiaNiemYet().getNgayapdung());
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText( getApplicationContext(),t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Thêm giá niêm yết ", "onFailure: " + t.getMessage());
                pd.dismiss();
            }
        });
    }

    private void deleteHangHoaByID(){
        apiService = ApiUtils.getApiService();
        apiService.deleteHangHoa(idHH).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                }
                else {
                    try {
                        Errors.initListError();
                        Toast.makeText(InsertHangHoaActivity.this, Errors.listErrors.get(response.code()), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(InsertHangHoaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setEvent(){
        imageView_Proct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // choose file image
                requestPermionAndPickImage();
            }
        });
        btnAddHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidInput()){
                    insertHangHoa();
                }
            }
        });

    }
    // request permison
    private void requestPermionAndPickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            chooseFileImage();
            return;
        }
        // request permison for model M or higher, if it'll crash app
        int result = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            chooseFileImage();
        } else {
            requestPermissions(new String[]{
                    READ_EXTERNAL_STORAGE}, READ_EXTERNAL_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != READ_EXTERNAL_REQUEST) return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseFileImage();
        } else {
            Toast.makeText(getApplicationContext(), "permission_denied",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void chooseFileImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    // on activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == InsertHangHoaActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            // get uri from gallery
            imageUriGallery = data.getData();
            Log.d("IMAGE URI ", "onActivityResult: " + imageUriGallery);
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUriGallery);
                // convert file image to bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView_Proct.setImageBitmap(bitmap);
                uploadImageToServer(imageUriGallery);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImageToServer(Uri uri) {
        if(uri == null) return;
        apiService = ApiUtils.getApiService();
        // show progress dialog
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading image...");
        pd.show();
        // get real path file image from gallery
        String realPathImg = RealPathUtil.getRealPath(this, uri);
        File file = new File(realPathImg);
        imageFileName = file.getName();
        // Create requestBody from choosen file image
        RequestBody requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)),file);

        // In retrofit 2 to upload file, we using Multipart
        // Declare MultipartBody.Part
        // hinhanh is KEY that define in server
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("hinhanh", file.getName(), requestBody);

        // Call api upload file
        apiService.uploadImage(filePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload image successfully ",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "uploadImageToServer: " + realPathImg);
                }
                else {
                    try {
                        Errors.initListError();
                        Toast.makeText(InsertHangHoaActivity.this, Errors.listErrors.get(response.code()), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Log.e(TAG, "onResponse Upload FILE: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InsertHangHoaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
