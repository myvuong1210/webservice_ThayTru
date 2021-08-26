package com.example.adminqlbh.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HangHoa implements Serializable {
    private String id;
    private String ten;
    private String moTa;
    private String anh;
    private int soLuongTon;
    private String khoiLuong;

    public HangHoa() {
    }

    public HangHoa(String id, String ten, String moTa, String anh, int soluongTon, String khoiLuong) {
        this.id = id;
        this.ten = ten;
        this.moTa = moTa;
        this.anh = anh;
        this.soLuongTon = soluongTon;
        this.khoiLuong = khoiLuong;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
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
        return id;
    }

}
