package com.example.adminqlbh.QuanLyPhieuNhapHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminqlbh.Models.PhieuNhapHang;
import com.example.adminqlbh.R;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class PhieuNhapHangAdapter extends ArrayAdapter<PhieuNhapHang> {
    private Context mContext;
    private List<PhieuNhapHang> listPhieuNhapHang;
    private int resource;

    public PhieuNhapHangAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PhieuNhapHang> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listPhieuNhapHang=objects;
    }
    Format format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_phieunhaphang,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.id= (TextView)convertView.findViewById(R.id.tvidpnh);
            viewHolder.ngaylap = (TextView)convertView.findViewById(R.id.tv_ngaylappnh);
            viewHolder.tongtien  = (TextView) convertView.findViewById(R.id.tv_tongtienpnh);
            viewHolder.idnv  = (TextView) convertView.findViewById(R.id.tv_idnv_pnh);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        PhieuNhapHang ct = listPhieuNhapHang.get(position);
        String str[] = ct.getNgayLap().split("-");
        int nam = Integer.parseInt(str[0]);
        int thang = Integer.parseInt(str[1]);
        int ngay=Integer.parseInt(str[2]);

        viewHolder.id.setText(ct.getId());
        viewHolder.tongtien.setText(format.format(ct.getTongTien()));
        viewHolder.ngaylap.setText(ngay+"/"+thang+"/"+nam);
        viewHolder.idnv.setText(ct.getIdNV());
        return convertView;
    }

    private class ViewHolder{
        private TextView id,tongtien,ngaylap,idnv;
        }

}
