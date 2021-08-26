package com.example.adminqlbh.QuanLyCT_PhieuDatHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.Models.CT_PhieuDatHang;
import com.example.adminqlbh.R;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CT_PhieuDatHangAdapter extends ArrayAdapter<CT_PhieuDatHang> {
    private Context mContext;
    private List<CT_PhieuDatHang> listCT_PhieuDatHang;
    private int resource;

    public CT_PhieuDatHangAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CT_PhieuDatHang> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listCT_PhieuDatHang=objects;
    }
    Format format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_chitietdathang,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.id= (TextView)convertView.findViewById(R.id.tv_idchitietdat);
            viewHolder.thanhtien = (TextView)convertView.findViewById(R.id.tv_thanhtien);
            viewHolder.soluong  = (TextView) convertView.findViewById(R.id.tv_soluong);
            viewHolder.idhh  = (TextView) convertView.findViewById(R.id.tv_idhh);
            viewHolder.idpdh = (TextView) convertView.findViewById(R.id.tv_idpdh);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CT_PhieuDatHang ct = listCT_PhieuDatHang.get(position);
        viewHolder.id.setText(ct.getId() );
        viewHolder.thanhtien.setText(format.format(ct.getThanhTien()));
        viewHolder.idhh.setText(ct.getIdHH());
        viewHolder.idpdh.setText(ct.getIdPhieuDatHang());
        viewHolder.soluong.setText(String.valueOf(ct.getSoLuong()) +" cái");
        return convertView;
    }

    private class ViewHolder{
        private TextView idpdh;
        private TextView idhh;
        private TextView id;
        private TextView thanhtien;
        private TextView soluong;
    }

}
