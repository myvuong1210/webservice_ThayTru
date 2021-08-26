package com.example.adminqlbh.UserSite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.adminqlbh.Api.RetrofitClient;

import com.example.adminqlbh.Models.GioHang;
import com.example.adminqlbh.R;
import com.example.adminqlbh.UserSite.activity.GioHangActivity;
import com.example.adminqlbh.UserSite.activity.UserHomePageActivity;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GioHangAdapter extends BaseAdapter {
    Context mContex;
    ArrayList<GioHang> arrGioHang;

    public GioHangAdapter(Context mContex, ArrayList<GioHang> arrGioHang) {
        this.mContex = mContex;
        this.arrGioHang = arrGioHang;
    }

    @Override
    public int getCount() {
        return arrGioHang.size();
    }

    @Override
    public Object getItem(int position) {
        return arrGioHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_user_item_giohang, null);
            viewHolder.imageViewGioHangSP = convertView.findViewById(R.id.imageviewGioHangSP);
            viewHolder.textViewTenGioHangSP = convertView.findViewById(R.id.txtViewGioHangTenSP);
            viewHolder.textViewGiaGioHangSP = convertView.findViewById(R.id.txtViewGioHangGiaSP);
            viewHolder.btnMinus = convertView.findViewById(R.id.btnMinusQuantity);
            viewHolder.btnValue = convertView.findViewById(R.id.btnValueQuantity);
            viewHolder.btnPlus = convertView.findViewById(R.id.btnPlusQuantity);
            viewHolder.textViewAvailableProct = convertView.findViewById(R.id.txtViewAvailableProct);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        GioHang gioHang = arrGioHang.get(position);
        Picasso.get().load(RetrofitClient.baseURLImage + gioHang.getHinhSP())
                .placeholder(R.drawable.no_image_icon)
                .into(viewHolder.imageViewGioHangSP);
        viewHolder.textViewTenGioHangSP.setText(gioHang.getTenSP());
        // Format String to vietnamese currency
        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        viewHolder.textViewGiaGioHangSP.setText("Giá : " + formatGia.format(gioHang.getGiaSP()));
        viewHolder.textViewAvailableProct.setText("Hàng có sẵn : " + gioHang.getSoluongTon() + " sản phẩm");
        viewHolder.btnValue.setText(String.valueOf(gioHang.getSoluongmua()));
        int slMua = Integer.parseInt(viewHolder.btnValue.getText().toString());
        if(slMua >= gioHang.getSoluongTon()){
            viewHolder.btnPlus.setVisibility(View.INVISIBLE);
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
        }
        else if(slMua <= 1){
            viewHolder.btnMinus.setVisibility(View.INVISIBLE);
        }
        else if(slMua >= 1){
            viewHolder.btnPlus.setVisibility(View.VISIBLE);
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
        }
        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMuaMoiNhat = Integer.parseInt(viewHolder.btnValue.getText().toString()) + 1;
                int slMuaHienTai = UserHomePageActivity.listGioHang.get(position).getSoluongmua();
                BigDecimal giaHienTai = UserHomePageActivity.listGioHang.get(position).getGiaSP();
                UserHomePageActivity.listGioHang.get(position).setSoluongmua(slMuaMoiNhat);
                // Tính lại giá thành tiền
                BigDecimal giaMoiNhat = giaHienTai.multiply(BigDecimal.valueOf(slMuaMoiNhat)).divide(BigDecimal.valueOf(slMuaHienTai));
                UserHomePageActivity.listGioHang.get(position).setGiaSP(giaMoiNhat);
                // Hiển thị lại giá sản phẩm
                // Format String to vietnamese currency
                Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                viewHolder.textViewGiaGioHangSP.setText("Giá : " + formatGia.format(giaMoiNhat));
                // Tính lại tổng số tiền
                GioHangActivity.tinhTongTien();
                // check hợp lệ số lượng tồn
                if(slMuaMoiNhat > gioHang.getSoluongTon() - 1){
                    viewHolder.btnPlus.setVisibility(View.INVISIBLE);
                    viewHolder.btnMinus.setVisibility(View.VISIBLE);
                    viewHolder.btnValue.setText(String.valueOf(slMuaMoiNhat));
                }
                else {
                    viewHolder.btnPlus.setVisibility(View.VISIBLE);
                    viewHolder.btnMinus.setVisibility(View.VISIBLE);
                    viewHolder.btnValue.setText(String.valueOf(slMuaMoiNhat));
                }
            }
        });
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int slMuaMoiNhat = Integer.parseInt(viewHolder.btnValue.getText().toString()) - 1;
                int slMuaHienTai = UserHomePageActivity.listGioHang.get(position).getSoluongmua();
                BigDecimal giaHienTai = UserHomePageActivity.listGioHang.get(position).getGiaSP();
                UserHomePageActivity.listGioHang.get(position).setSoluongmua(slMuaMoiNhat);
                // Tính lại giá thành tiền
                BigDecimal giaMoiNhat = giaHienTai.multiply(BigDecimal.valueOf(slMuaMoiNhat)).divide(BigDecimal.valueOf(slMuaHienTai));
                UserHomePageActivity.listGioHang.get(position).setGiaSP(giaMoiNhat);
                // Hiển thị lại giá sản phẩm
                // Format String to vietnamese currency
                Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                viewHolder.textViewGiaGioHangSP.setText("Giá : " + formatGia.format(giaMoiNhat));
                // Tính lại tổng số tiền
                GioHangActivity.tinhTongTien();
                // check hợp lệ số lượng tồn
                if(slMuaMoiNhat < 2){
                    viewHolder.btnPlus.setVisibility(View.VISIBLE);
                    viewHolder.btnMinus.setVisibility(View.INVISIBLE);
                    viewHolder.btnValue.setText(String.valueOf(slMuaMoiNhat));
                }
                else {
                    viewHolder.btnPlus.setVisibility(View.VISIBLE);
                    viewHolder.btnMinus.setVisibility(View.VISIBLE);
                    viewHolder.btnValue.setText(String.valueOf(slMuaMoiNhat));
                }
            }
        });
        return convertView;
    }

    public class ViewHolder{
       ImageView imageViewGioHangSP;
       TextView textViewTenGioHangSP, textViewGiaGioHangSP, textViewAvailableProct;
       Button btnMinus, btnValue, btnPlus;
    }
}
