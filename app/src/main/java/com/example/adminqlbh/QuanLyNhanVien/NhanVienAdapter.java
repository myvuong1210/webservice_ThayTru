package com.example.adminqlbh.QuanLyNhanVien;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminqlbh.Models.NhanVien;
import com.example.adminqlbh.R;

import java.util.List;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    private Context mContext;
    private List<NhanVien> listNhanVien;
    private int resource;

    public NhanVienAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NhanVien> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listNhanVien=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_nhanvien,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView)convertView.findViewById(R.id.tv_idnv);
            viewHolder.ten = (TextView)convertView.findViewById(R.id.tv_tennv);
            viewHolder.email  = (TextView) convertView.findViewById(R.id.tv_emailnv);
            viewHolder.diachi  = (TextView) convertView.findViewById(R.id.tv_diachinv);
            viewHolder.sdt = (TextView) convertView.findViewById(R.id.tv_sdtnv);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NhanVien hh = listNhanVien.get(position);
        viewHolder.email.setMaxLines(1);
        viewHolder.email.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.sdt.setMaxLines(1);
        viewHolder.sdt.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.diachi.setMaxLines(1);
        viewHolder.diachi.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.id.setText( hh.getId() );
        viewHolder.ten.setText(hh.getHoTen());
        viewHolder.diachi.setText(hh.getDiaChi());
        viewHolder.sdt.setText(hh.getSdt());
        viewHolder.email.setText(hh.getEmail());

        return convertView;
    }

    private class ViewHolder{
        private TextView email;
        private TextView id;
        private TextView ten;
        private TextView sdt;
        private TextView diachi;
    }

}
