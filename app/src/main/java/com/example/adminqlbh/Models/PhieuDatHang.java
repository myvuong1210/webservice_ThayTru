package com.example.adminqlbh.Models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class PhieuDatHang {
    @SerializedName("id")
    String id;

    @SerializedName("ngayLap")
    String ngayLap;

    @SerializedName("tongTien")
    BigDecimal tongTien;

    @SerializedName("trangThai")
    int trangThai;

    @SerializedName("idNV")
    String idNV;

    @SerializedName("idKH")
    String idKH;

    @SerializedName("diaChi")
    String diaChi;

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public PhieuDatHang() {
    }

    public String getIdNV() {
        return idNV;
    }


    public void setIdNV(String idNV) {
        this.idNV = idNV;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(String ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }


    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString(){
        return this.id;
    }


}
