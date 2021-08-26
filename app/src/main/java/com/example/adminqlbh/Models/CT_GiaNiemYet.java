package com.example.adminqlbh.Models;

import java.math.BigDecimal;

public class CT_GiaNiemYet {
    private String ngayApDung;
    private BigDecimal gia;
    private String idHH;
    public CT_GiaNiemYet() {
    }

    public String getNgayapdung() {
        return ngayApDung;
    }

    public void setNgayapdung(String ngayapdung) {
        this.ngayApDung = ngayapdung;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public String getIdHH() {
        return idHH;
    }

    public void setIdHH(String idHH) {
        this.idHH = idHH;
    }
}
