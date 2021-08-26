package com.example.adminqlbh.Models;

import java.math.BigDecimal;

public class GioHang {
    private String idSP;
    private String tenSP;
    private BigDecimal giaSP;
    private String hinhSP;
    private int soluongmua;
    private int soluongTon;

    public int getSoluongTon() {
        return soluongTon;
    }

    public void setSoluongTon(int soluongTon) {
        this.soluongTon = soluongTon;
    }

    public int getSoluongmua() {
        return soluongmua;
    }

    public void setSoluongmua(int soluongmua) {
        this.soluongmua = soluongmua;
    }

    public GioHang(String idSP, String tenSP, BigDecimal giaSP, String hinhSP, int soluongmua, int soluongTon) {
        this.idSP = idSP;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.hinhSP = hinhSP;
        this.soluongmua = soluongmua;
        this.soluongTon = soluongTon;
    }

    public String getIdSP() {
        return idSP;
    }

    public void setIdSP(String idSP) {
        this.idSP = idSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public BigDecimal getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(BigDecimal giaSP) {
        this.giaSP = giaSP;
    }

    public String getHinhSP() {
        return hinhSP;
    }

    public void setHinhSP(String hinhSP) {
        this.hinhSP = hinhSP;
    }


}
