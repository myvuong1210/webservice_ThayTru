using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Threading.Tasks;

namespace QLBH_API.Validation
{
    class ValidationData
    {
        public static bool checkSDT(string sdt)
        {
            if (sdt.Length != 10) return false;
            try
            {
                int res = int.Parse(sdt);
            }
            catch (Exception)
            {
                return false;
            }
            return true;
        }

        public static bool checkEmail(string emailaddress)
        {
            try
            {
                MailAddress m = new MailAddress(emailaddress);
                return true;
            }
            catch (FormatException e)
            {
                return false;
            }
        }
        public static bool checkSoDuong(string number)
        {
            try
            {
                Int64.Parse(number);
                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }
    }
}
