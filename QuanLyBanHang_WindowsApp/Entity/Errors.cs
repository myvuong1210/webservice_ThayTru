using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace QLBH_API.Entity
{
    class Errors
    {
        public static Dictionary<string, string> listError;

        public static void generateListError()
        {
            listError = new Dictionary<string, string>();
            listError.Clear();
            listError.Add("1", "Đối tượng đã tồn tại. Vui lòng thử lại sau");
            listError.Add("2", "Lỗi server");
            listError.Add("3", "Không tìm thấy đối tượng phù hợp");
            listError.Add("4", "Đường dẫn sai");
            listError.Add("5", "Không thể xóa");
        }
    }
}
