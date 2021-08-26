package com.example.adminqlbh.Models;

import java.io.Serializable;
import java.math.BigDecimal;

public class HangHoaFull implements Serializable {
    private String id;
    private String ten;
    private BigDecimal gia;
    private String moTa;
    private String anh;
    private int soLuongTon;
    private String khoiLuong;

    public HangHoaFull() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getSoluongTon() {
        return soLuongTon;
    }

    public void setSoluongTon(int soluongTon) {
        this.soLuongTon = soluongTon;
    }

    public String getKhoiLuong() {
        return khoiLuong;
    }

    public void setKhoiLuong(String khoiLuong) {
        this.khoiLuong = khoiLuong;
    }

    @Override
    public String toString() {
        return "HangHoaFull{" +
                "id='" + id + '\'' +
                ", ten='" + ten + '\'' +
                ", gia=" + gia +
                ", moTa='" + moTa + '\'' +
                ", anh='" + anh + '\'' +
                ", soluongTon=" + soLuongTon +
                ", khoiLuong='" + khoiLuong + '\'' +
                '}';
    }
}
