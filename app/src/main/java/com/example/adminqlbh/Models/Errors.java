package com.example.adminqlbh.Models;

import java.util.HashMap;

public class Errors {

    public static HashMap<Integer,String> listErrors;

    public static void initListError() {
        listErrors = new HashMap<Integer, String>();
        listErrors.clear();
        listErrors.put(400,"Lỗi trùng khóa chính"); //1
        listErrors.put(500,"Lỗi server");           //2
        listErrors.put(404,"Không tìm thấy mẫu tin");  //3
        listErrors.put(406,"Không thể xóa");        //5
    }
}

