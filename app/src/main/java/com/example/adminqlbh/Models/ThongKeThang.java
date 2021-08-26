package com.example.adminqlbh.Models;

public class ThongKeThang {
    private String id;
    private String thang;
    private String ten;
    private int tong;

    public ThongKeThang() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThang() {
        return thang;
    }

    public void setThang(String thang) {
        this.thang = thang;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getTong() {
        return tong;
    }

    public void setTong(int tong) {
        this.tong = tong;
    }

    @Override
    public String toString() {
        return "ThongKeThang{" +
                "id='" + id + '\'' +
                ", thang='" + thang + '\'' +
                ", ten='" + ten + '\'' +
                ", tong=" + tong +
                '}';
    }
}
