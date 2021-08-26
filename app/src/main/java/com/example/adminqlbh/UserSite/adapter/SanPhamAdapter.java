package com.example.adminqlbh.UserSite.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminqlbh.Api.AsyncTaskDownloadImage;
import com.example.adminqlbh.Api.RetrofitClient;
import com.example.adminqlbh.Models.HangHoa;
import com.example.adminqlbh.Models.HangHoaFull;
import com.example.adminqlbh.R;
import com.example.adminqlbh.UserSite.activity.ChiTietSanPhamActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.itemViewHolder> {
    Context mContext;
    List<HangHoaFull> listHangHoaFull;

    public SanPhamAdapter(Context mContext, List<HangHoaFull> listHangHoaFull) {
        this.mContext = mContext;
        this.listHangHoaFull = listHangHoaFull;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_item_sanpham, null);
        itemViewHolder itemViewHolder = new itemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(itemViewHolder holder, int position) {
        HangHoaFull hanghoa = listHangHoaFull.get(position);
        //new AsyncTaskDownloadImage(holder.anhSP).execute(hanghoa.getAnh());
        Picasso.get().load(RetrofitClient.baseURLImage + hanghoa.getAnh())
                .placeholder(R.drawable.no_image_icon)
                .into(holder.anhSP);
        // set max size text view
        holder.textViewTenSP.setMaxLines(2);
        // set "..." end line text view
        holder.textViewTenSP.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewTenSP.setText(hanghoa.getTen());
        // Format String to vietnamese currency
        Format formatGia = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.textViewGiaSP.setText("Giá : " + formatGia.format(hanghoa.getGia()));
    }

    @Override
    public int getItemCount() {
        return listHangHoaFull.size();
    }

    public class itemViewHolder extends RecyclerView.ViewHolder{
        private ImageView anhSP;
        private TextView textViewTenSP, textViewGiaSP;

        public itemViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ
            anhSP = itemView.findViewById(R.id.imageviewSanPham);
            textViewTenSP = itemView.findViewById(R.id.textviewTenSP);
            textViewGiaSP = itemView.findViewById(R.id.textviewGiaSP);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ChiTietSanPhamActivity.class);
                    intent.putExtra("sanpham", listHangHoaFull.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
