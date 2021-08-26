package com.example.adminqlbh.QuanLyPhieuDatHang;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.R;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CT_PhieuDatHangAdapter extends BaseAdapter {
    public List<CT_PhieuDatHang> listCT = new ArrayList<>();

    public CT_PhieuDatHangAdapter(List<CT_PhieuDatHang> listCT) {
        this.listCT = listCT;
    }

    @Override
    public int getCount() {
        return listCT.size();
    }

    @Override
    public Object getItem(int position) {
        return listCT.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null)
            view = View.inflate(parent.getContext(), R.layout.layout_item_chitietdathang, null);
        else view = convertView;
        CT_PhieuDatHang ct = listCT.get(position);
        ((TextView)view.findViewById(R.id.tv_idhh)).setText(ct.getIdHH());
        ((TextView)view.findViewById(R.id.tv_idchitietdat)).setText(ct.getId());
        ((TextView)view.findViewById(R.id.tv_thanhtien)).setText(formatGia.format(ct.getThanhTien()));
        ((TextView)view.findViewById(R.id.tv_soluong)).setText(String.valueOf(ct.getSoLuong()+" cái"));
        ((TextView)view.findViewById(R.id.tv_idpdh)).setText(ct.getIdPhieuDatHang());
        return view;
    }
}