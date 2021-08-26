package com.example.adminqlbh.UserSite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminqlbh.Api.AsyncTaskLoadProduct;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.R;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChiTietDonHangAdapter extends BaseAdapter {
    List<CT_PhieuDatHang> listCT_Donhang;
    Context mContext;

    class viewHolder {
        ImageView anhSP;
        TextView tvTenSP, tvSoluongmua, tvThanhtien;
    }

    public ChiTietDonHangAdapter(List<CT_PhieuDatHang> listCT_Donhang, Context mContext) {
        this.listCT_Donhang = listCT_Donhang;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listCT_Donhang.size();
    }

    @Override
    public Object getItem(int position) {
        return listCT_Donhang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder;
        if(convertView == null){
            viewHolder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_user_item_chitietdonhang, null);
            viewHolder.anhSP = convertView.findViewById(R.id.imageviewGioHangSP);
            viewHolder.tvTenSP = convertView.findViewById(R.id.tvCT_DonhangTenSP);
            viewHolder.tvSoluongmua = convertView.findViewById(R.id.tvCT_DonhangSoluongmua);
            viewHolder.tvThanhtien = convertView.findViewById(R.id.tvCT_DonhangThanhtien);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ChiTietDonHangAdapter.viewHolder) convertView.getTag();
        }
        CT_PhieuDatHang ct_phieuDatHang = listCT_Donhang.get(position);
        new AsyncTaskLoadProduct(viewHolder.anhSP, viewHolder.tvTenSP).execute(ct_phieuDatHang.getIdHH());
        viewHolder.tvSoluongmua.setText(String.valueOf(ct_phieuDatHang.getSoLuong()) + " sản phẩm | ");
        // Format String to vietnamese currency
        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        viewHolder.tvThanhtien.setText(formatGia.format(ct_phieuDatHang.getThanhTien()));
        return convertView;
    }
}
