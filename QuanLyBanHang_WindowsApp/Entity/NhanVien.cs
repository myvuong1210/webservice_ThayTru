using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH_API.Entity
{
    class NhanVien
    {
        public string id { get; set; }
        public string hoTen { get; set; }
        public string matKhau { get; set; }
        public string sdt { get; set; }
        public string diaChi { get; set; }
        public string email { get; set; }
        public bool quyen { get; set; }

        public void chuanHoaDuLieu()
        {
            hoTen = Program.convertToUTF8(hoTen);
            diaChi = Program.convertToUTF8(diaChi);
        }
    }
}
