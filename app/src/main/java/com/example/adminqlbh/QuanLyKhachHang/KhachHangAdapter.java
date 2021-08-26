package com.example.adminqlbh.QuanLyKhachHang;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminqlbh.Api.AsyncTaskDownloadImage;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.KhachHang;
import com.example.adminqlbh.R;

import java.util.List;

public class KhachHangAdapter extends ArrayAdapter<KhachHang> {
    private Context mContext;
    private List<KhachHang> listKhachHang;
    private int resource;

    public KhachHangAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<KhachHang> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listKhachHang=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_khachhang,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView)convertView.findViewById(R.id.tv_idkh);
            viewHolder.ten = (TextView)convertView.findViewById(R.id.tv_tenkh);
            viewHolder.email  = (TextView) convertView.findViewById(R.id.tv_emailkh);
            viewHolder.diachi  = (TextView) convertView.findViewById(R.id.tv_diachikh);
            viewHolder.sdt = (TextView) convertView.findViewById(R.id.tv_sdtkh);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.email.setMaxLines(1);
        viewHolder.email.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.sdt.setMaxLines(1);
        viewHolder.sdt.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.diachi.setMaxLines(1);
        viewHolder.diachi.setEllipsize(TextUtils.TruncateAt.END);

        KhachHang hh = listKhachHang.get(position);
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
