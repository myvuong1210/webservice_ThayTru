package com.example.adminqlbh.QuanLyPhieuDatHang;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminqlbh.Models.PhieuDatHang;
import com.example.adminqlbh.R;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhieuDatHang_Adapter extends BaseAdapter {
    public List<PhieuDatHang> listPhieu = new ArrayList<>();

    public PhieuDatHang_Adapter(List<PhieuDatHang> listPhieu) {
        this.listPhieu = listPhieu;
    }


    @Override
    public int getCount() {
        return listPhieu.size();
    }

    @Override
    public Object getItem(int position) {
        return listPhieu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null)
            view = View.inflate(parent.getContext(), R.layout.layout_item_phieudathang, null);
        else
            view = convertView;

        String str[] = listPhieu.get(position).getNgayLap().split("-");
        int nam = Integer.parseInt(str[0]);
        int thang = Integer.parseInt(str[1]);
        int ngay=Integer.parseInt(str[2]);

        ((TextView)view.findViewById(R.id.textView_adapterPhieu_diaChi)).setMaxLines(1);
        ((TextView)view.findViewById(R.id.textView_adapterPhieu_diaChi)).setEllipsize(TextUtils.TruncateAt.END);
        ((TextView)view.findViewById(R.id.textView_adapterPhieu_id)).setText( listPhieu.get(position).getId());
        ((TextView)view.findViewById(R.id.textView_adapterPhieu_KhachHang)).setText(listPhieu.get(position).getIdKH());
        ((TextView)view.findViewById(R.id.textView_adapterPhieu_NgayLap)).setText(ngay+"/"+thang+"/"+nam);
        ((TextView)view.findViewById(R.id.textView_adapterPhieu_diaChi)).setText(listPhieu.get(position).getDiaChi());



        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));


        ((TextView)view.findViewById(R.id.textView_adapterPhieu_TongTien)).setText(formatGia.format(listPhieu.get(position).getTongTien()));
        switch (listPhieu.get(position).getTrangThai()){
            case -1:{
                ((TextView)view.findViewById(R.id.textView_adapterPhieu_trangThai)).setText("Đơn bị hủy");
                break;
            }
            case 0:{
                ((TextView)view.findViewById(R.id.textView_adapterPhieu_trangThai)).setText("Chờ xác nhận");
                break;
            }
            case 1:{
                ((TextView)view.findViewById(R.id.textView_adapterPhieu_trangThai)).setText("Đang giao hàng");
                break;
            }
            case 2:{
                ((TextView)view.findViewById(R.id.textView_adapterPhieu_trangThai)).setText("Hoàn thành");
                break;
            }
            case 3:{
                ((TextView)view.findViewById(R.id.textView_adapterPhieu_trangThai)).setText("Giao hàng thất bại");
                break;
            }
        }


        return view;
    }
}
