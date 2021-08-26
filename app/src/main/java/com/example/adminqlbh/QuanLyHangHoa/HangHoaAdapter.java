package com.example.adminqlbh.QuanLyHangHoa;

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
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.R;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HangHoaAdapter extends ArrayAdapter<HangHoa> {
    private Context mContext;
    private List<HangHoa> listHangHoa;
    private int resource;

    public HangHoaAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HangHoa> objects) {
        super(context, resource, objects);
        this.mContext= context;
        this.resource=resource;
        this.listHangHoa=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_hanghoa,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.idHangHoa = (TextView)convertView.findViewById(R.id.tv_idHanghoa);
            viewHolder.tenHangHoa = (TextView)convertView.findViewById(R.id.tv_tenHanghoa);
            viewHolder.khoiluong  = (TextView) convertView.findViewById(R.id.tv_khoiluongHH);
            viewHolder.slTon  = (TextView) convertView.findViewById(R.id.tv_slTonHH);
            viewHolder.imgHangHoa = (ImageView) convertView.findViewById(R.id.iv_hanghoa);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HangHoa hh = listHangHoa.get(position);
//        new AsyncTaskDownloadImage(viewHolder.imgHangHoa).execute(hh.getAnh());
        Picasso.get().load(RetrofitClient.baseURLImage + hh.getAnh())
                .placeholder(R.drawable.no_image_icon)
                .into(viewHolder.imgHangHoa);
        viewHolder.idHangHoa.setText( hh.getId() );
        // set max size text view
        viewHolder.tenHangHoa.setMaxLines(1);
        // set "..." end line text view
        viewHolder.tenHangHoa.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tenHangHoa.setText(hh.getTen());
        viewHolder.slTon.setText(String.valueOf(hh.getSoluongTon()));
        viewHolder.khoiluong.setText( hh.getKhoiLuong());

        return convertView;
    }

    private class ViewHolder{
        private ImageView imgHangHoa;
        private TextView idHangHoa;
        private TextView tenHangHoa;
        private TextView khoiluong;
        private TextView slTon;
    }

}
