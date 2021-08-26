package com.example.adminqlbh.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CT_PhieuDatHang implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("soLuong")
    int soLuong;

    @SerializedName("thanhTien")
    int thanhTien;

    @SerializedName("idHH")
    String idHH;

    @SerializedName("idPhieuDatHang")
    String idPhieuDatHang;

    public CT_PhieuDatHang() {
    }

    public CT_PhieuDatHang(String id, int soLuong, int thanhTien, String idHH, String idPhieuDatHang) {
        this.id = id;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
        this.idHH = idHH;
        this.idPhieuDatHang = idPhieuDatHang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getIdHH() {
        return idHH;
    }

    public void setIdHH(String idHH) {
        this.idHH = idHH;
    }

    public String getIdPhieuDatHang() {
        return idPhieuDatHang;
    }

    public void setIdPhieuDatHang(String idPhieuDatHang) {
        this.idPhieuDatHang = idPhieuDatHang;
    }
}

