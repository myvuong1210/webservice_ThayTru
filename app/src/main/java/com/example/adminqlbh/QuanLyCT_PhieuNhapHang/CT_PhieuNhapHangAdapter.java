package com.example.adminqlbh.QuanLyCT_PhieuNhapHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.adminqlbh.Models.CT_PhieuNhapHang;
import com.example.adminqlbh.R;

import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CT_PhieuNhapHangAdapter extends ArrayAdapter<CT_PhieuNhapHang> {
    private Context mContext;
    private List<CT_PhieuNhapHang> listCT_PhieuNhapHang;
    private int resource;

    public CT_PhieuNhapHangAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CT_PhieuNhapHang> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listCT_PhieuNhapHang=objects;
    }
    Format format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_chitietnhaphang,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.id= (TextView)convertView.findViewById(R.id.tv_idchitietnhap);
            viewHolder.thanhtien = (TextView)convertView.findViewById(R.id.tv_thanhtien);
            viewHolder.soluong  = (TextView) convertView.findViewById(R.id.tv_soluong);
            viewHolder.idhh  = (TextView) convertView.findViewById(R.id.tv_idhh);
            viewHolder.idpnh = (TextView) convertView.findViewById(R.id.tv_idpnh);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CT_PhieuNhapHang ct = listCT_PhieuNhapHang.get(position);
        viewHolder.id.setText( ct.getId() );
        viewHolder.thanhtien.setText(format.format(ct.getThanhTien()));
        viewHolder.idhh.setText( ct.getIdhh());
        viewHolder.idpnh.setText(ct.getIdPhieuNhapHang());
        viewHolder.soluong.setText( String.valueOf(ct.getSoLuong())+" cái");

        return convertView;
    }

    private class ViewHolder{
        private TextView idpnh;
        private TextView idhh;
        private TextView id;
        private TextView thanhtien;
        private TextView soluong;
    }

}
