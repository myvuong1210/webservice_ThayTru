package com.example.adminqlbh.Api;

import com.example.adminqlbh.Models.CT_GiaNhap;
import com.example.adminqlbh.Models.CT_GiaNiemYet;
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.Models.GiaHangHoa;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.HangHoaFull;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.Models.Login;
import com.example.adminqlbh.Models.ThongKeThang;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.Models.PhieuNhapHang;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    //========================Login==========================
    @POST("login")
    Call<Integer> sign_in(@Body Login account);

    //========================Hàng hóa=======================
    //Get list products
    @GET("hanghoa")
    Call<List<HangHoa>> getListProduct();

    @POST("hanghoa")
    Call<String> postHangHoa(@Body HangHoa hanghoa);

    @PUT("hanghoa")
    Call<String> updateHH(@Body HangHoa hanghoa);
    // Delete product
    @DELETE("hanghoa/{id}")
    Call<ResponseBody> deleteHangHoa(@Path("id") String id);
    // Get Hanghoa from id
    @GET("hanghoa/{id}")
    Call<HangHoa> getProductById(@Path("id") String id);
    // Get list HangHoa with giaNiemYet
    @GET("hanghoa/gia")
    Call<List<HangHoaFull>> geHangHoaFull();


    //========================Chi tiết giá niêm yết================
    @POST("chitietgianiemyet")
    Call<String> createGiaNiemYet(@Body CT_GiaNiemYet ct_giaNiemYet);
    //
    @PUT("chitietgianiemyet")
    Call<String> updateGiaNiemYet(@Body CT_GiaNiemYet ct_giaNiemYet);
    //
//    //Get Gia niêm yết from id Hanghoa
    @GET("hanghoa/gia/{id}")
    Call<GiaHangHoa> getPriceFromIDhh(@Path("id") String id);



    @POST("chitietgianhap")
    Call<String> createGiaNhap(@Body CT_GiaNhap ct_gianhap);
    //
    @PUT("chitietgianhap")
    Call<String> updateGiaNhap(@Body CT_GiaNhap ct_gianhap);
    //
//    //Get Gia niêm yết from id Hanghoa
    @GET("hanghoa/gianhaphang/{id}")
    Call<GiaHangHoa> getGiaNhapFromIDhh(@Path("id") String id);



    @GET("khachhang")
    Call<List<KhachHang>> getListKhachHang();

    @POST("khachhang")
    Call<String> postKhachHang(@Body KhachHang khachhang);

    @PUT("khachhang")
    Call<String> updateKH(@Body KhachHang khachhang);

    @DELETE("khachhang/{id}")
    Call<ResponseBody> deleteKhachHang(@Path("id") String id);

    //
//    //Delete product
    @HTTP(method = "DELETE", path = "khachhang", hasBody = true)
    Call<String> deleteKH(@Body KhachHang khachhang);
    //
//    // Get khachhang from id
    @GET("khachhang/{id}")
    Call<KhachHang> getKhachHangById(@Path("id") String id);
//


    @GET("nhanvien")
    Call<List<NhanVien>> getListNhanVien();

    @POST("nhanvien")
    Call<String> postNhanVien(@Body NhanVien nhanvien);

    @PUT("nhanvien")
    Call<String> updateNV(@Body NhanVien nhanvien);
    //
//    //Delete product
    @DELETE("nhanvien/{id}")
    Call<ResponseBody> deleteNhanVien(@Path("id") String id);

    //
//    // Get NhanVien from id
    @GET("nhanvien/{id}")
    Call<NhanVien> getNhanVienById(@Path("id") String id);
//

    @GET("chitietnhaphang")
    Call<List<CT_PhieuNhapHang>> getListCT_PhieuNhapHang();

    @POST("chitietnhaphang")
    Call<String> postCT_PhieuNhapHang(@Body CT_PhieuNhapHang chitietnhaphang);

    @PUT("chitietnhaphang")
    Call<String> updateCTPNH(@Body CT_PhieuNhapHang chitietnhaphang);
    //
    @DELETE("chitietnhaphang/{id}")
    Call<ResponseBody> deleteCTPNH(@Path("id") String id);
    //    // Get CT_PhieuNhapHang from id
    @GET("chitietnhaphang/{id}")
    Call<CT_PhieuNhapHang> getCT_PhieuNhapById(@Path("id") String id);



    //========================Thông kê hàng hóa bán chạy trong tháng================
    @GET("thongkethang/{thang}")
    Call<List<ThongKeThang>> getListTopSellByMonth(@Path("thang") String thang);

    @GET("phieunhaphang")
    Call<List<PhieuNhapHang>> getListPhieuNhapHang();

    //========================Chi tiết phiếu đặt hàng=======================
    @DELETE("chitietdathang/{id}")
    Call<ResponseBody> deleteCTPDH(@Path("id") String id);

    @DELETE("chitietdathang")
    @HTTP(method = "DELETE", path = "chitietdathang", hasBody = true)
    Call<CT_PhieuDatHang> remove (@Body CT_PhieuDatHang content);

    @GET("chitietdathang")
    Call<List<CT_PhieuDatHang>> getListCT_PhieuDatHang();

    @POST("chitietdathang")
    Call<String> postCT_PhieuDatHang(@Body CT_PhieuDatHang chitietdathang);

    @PUT("chitietdathang")
    Call<String> updateCTPDH(@Body CT_PhieuDatHang chitietdathang);

    @DELETE("chitietdathang/{id}")
    Call<ResponseBody> deleteCT_PDH(@Path("id") String id);

    @POST("chitietdathang")
    Call<String> insertCT_PhieuDatHang(@Body CT_PhieuDatHang ct_phieuDatHang);
    //========================Phiếu đặt hàng=================================

    // Doi trang thai Phieu dat hang
    @PUT("phieudathang")
    Call<String> updatePhieuDatHang(@Body PhieuDatHang phieuDatHang);

    //Get List Don dat hang
    @GET("phieudathang")
    Call<List<PhieuDatHang>> getPhieuDatHang();

    // insert phiếu đặt hàng
    @POST("phieudathang")
    Call<String> insertPhieuDatHang(@Body PhieuDatHang phieuDatHang);

    @POST("dathang")
    Call<String> insertListCT_PDH(@Body ArrayList<CT_PhieuDatHang> arrCTPDH);

    //========================Phiếu nhập hàng=================================
    @POST("phieunhaphang")
    Call<String> postPhieuNhapHang(@Body PhieuNhapHang phieunhaphang);

    @PUT("phieunhaphang")
    Call<String> updatePNH(@Body PhieuNhapHang phieunhaphang);

    @GET("phieunhaphang/{id}")
    Call<PhieuNhapHang> getPNHById(@Path("id") String id);

    @DELETE("phieunhaphang/{id}")
    Call<ResponseBody> deletePNH(@Path("id") String id);

    //========================Check đơn hàng=================================

    // thông tin chi tiết đơn hàng theo id phiếu đặt hàng
    @GET("thongtinphieudathang/{id}")
    Call<List<CT_PhieuDatHang>> getCT(@Path("id") String id);

    // thông tin đơn đặt hàng theo id khách hàng
    @GET("dondathang/{id}")
    Call<List<PhieuDatHang>> getListPDHByID(@Path("id") String id);

    //========================Upload hình ảnh=================================
    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage (@Part MultipartBody.Part image);

    //========================Send mail=================================
    @POST("send/{id}")
    Call<String> sendMail(@Path("id")String id);

}
