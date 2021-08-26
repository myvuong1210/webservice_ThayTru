package com.example.adminqlbh.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PhieuNhapHang implements Serializable {
    @SerializedName("id")
    String id;

    @SerializedName("ngayLap")
    String ngayLap;

    @SerializedName("tongTien")
    BigDecimal tongTien;

    @SerializedName("idNV")
    String idNV;

    public PhieuNhapHang() {
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

    public PhieuNhapHang( String id, String ngayLap, BigDecimal tongTien, String idNV) {
         this.id = id;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.idNV = idNV;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public String getIdNV() {
        return idNV;
    }

    public void setIdNV(String idNV) {
        this.idNV = idNV;
    }

    @Override
    public String toString() {
        return id;
    }
}