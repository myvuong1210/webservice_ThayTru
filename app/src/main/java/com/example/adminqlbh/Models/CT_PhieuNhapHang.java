package com.example.adminqlbh.Models;

import java.io.Serializable;

public class  CT_PhieuNhapHang implements Serializable {


    private String id , idhh, idPhieuNhapHang;

    private int thanhTien, soLuong;

    public String getIdhh() {
        return idhh;
    }

    public void setIdhh(String idhh) {
        this.idhh = idhh;
    }

    public String getIdPhieuNhapHang() {
        return idPhieuNhapHang;
    }

    public void setIdPhieuNhapHang(String idPhieuNhapHang) {
        this.idPhieuNhapHang = idPhieuNhapHang;
    }

    public CT_PhieuNhapHang(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }


    @Override
    public String toString() {
        return id;
    }


}
