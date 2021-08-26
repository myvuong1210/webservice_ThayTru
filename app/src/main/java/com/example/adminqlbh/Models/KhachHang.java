package com.example.adminqlbh.Models;


import java.io.Serializable;

public class KhachHang implements Serializable {
    private String id;
    private String hoTen;
    private String diaChi;
    private String sdt;
    private String email;
    private String matKhau;

    public KhachHang() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatkhau() {
        return matKhau;
    }

    public void setMatkhau(String matKhau) {
        this.matKhau = matKhau;
    }

//    @Override
//    public String toString(){
//        return "*MÃ KHÁCH HÀNG: " + id + "\n" + "*HỌ TÊN KHÁCH HÀNG: " + hoTen+ "\n" + "*ĐỊA CHỈ KHÁCH HÀNG: "+diaChi+"\n"+"*SỐ ĐIỆN THOẠI: "+sdt +"\n"+"*EMAIL KHÁCH HÀNG: " +email;
//    }

    @Override
    public String toString() {
        return id;
    }

}
