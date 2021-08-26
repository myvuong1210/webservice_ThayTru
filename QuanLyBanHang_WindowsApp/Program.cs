using DevExpress.Skins;
using DevExpress.UserSkins;
using QLBH_API.Entity;
using QLBH_API.Forms;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Windows.Forms;

namespace QLBH_API
{
    static class Program
    {
        
        public static frm_Main frmChinh;
        public static Form_Login FrmLogin;
        public static frmTaoTK FrmTaoTK;
        public static String mGroup = "";

        //public static string baseURL = "https://quan-ly-ban-hang-api.herokuapp.com/";
        public static string baseURL = "http://116.109.64.101:8080/";
        public static string convertToUTF8(string data)
        {
            // đưa về dịnh dạng UTF-8
            var utf8 = Encoding.UTF8;
            byte[] utfBytes = utf8.GetBytes(data);
            data = utf8.GetString(utfBytes, 0, utfBytes.Length);
            return data;
        }
        public static Bitmap loadImage(string url)
        {
            
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            Stream stream = client.OpenRead(url);
            Bitmap bitmap = new Bitmap(stream);

            stream.Flush();
            stream.Close();
            client.Dispose();

            return bitmap;
        }
        public static Bitmap resizeImage(Bitmap bitmap, int width, int height)
        {
            Bitmap res = new Bitmap(width, height);
            using (Graphics g = Graphics.FromImage(res))
            {
                g.DrawImage(bitmap, 0, 0, width, height);
            }
            return res;
        }
        public static string generateID(string maxID)
        {
            maxID = maxID.Trim();
            string number = maxID.Substring(maxID.Length - 3);
            Console.WriteLine(number);
            string head = maxID.Substring(0, maxID.Length - 3);
            Console.WriteLine(head);
            int num = int.Parse(number) + 1;

            if (num < 10) return head + "00" + num;
            if (num < 100) return head + "0" + num;
            return head + num;

        }
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

            BonusSkins.Register();
            SkinManager.EnableFormSkins();

            Program.FrmLogin = new Form_Login();
            Application.Run(Program.FrmLogin);
        }
    }
}
