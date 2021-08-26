using QLBH_API.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.IO;
using System.Windows.Forms;

namespace QLBH_API.Services
{
    class Service_NhanVien
    {

        string url = Program.baseURL + "nhanvien";
        public static string errorMessage = "";
        public static string errorCode;
        
        public List<NhanVien> getListNhanVien()
        {
            List<NhanVien> nhanViens = null;

            WebClient wc = new WebClient();
            wc.Encoding = System.Text.Encoding.UTF8;
            try
            {
                string data = wc.DownloadString(url);

                Console.WriteLine(data);

                nhanViens = JsonConvert.DeserializeObject<List<NhanVien>>(data);

            }
            catch (WebException e)
            {
                using (StreamReader r = new StreamReader(
                    e.Response.GetResponseStream()))
                {
                    string responseContent = r.ReadToEnd();
                    errorMessage = Errors.listError[responseContent];
                    errorCode = responseContent;
                    Console.WriteLine(errorMessage);
                }
            }
            return nhanViens;
        }

        public NhanVien getNhanVien(string id)
        {
            WebClient client = new WebClient();
            NhanVien nhanVien = null;
            client.Encoding = System.Text.Encoding.UTF8;

            try
            {
                String data = client.DownloadString(url + string.Format("/" + id));
                nhanVien = JsonConvert.DeserializeObject<NhanVien>(data);
            }
            catch (WebException e)
            {
                using (StreamReader r = new StreamReader(
                    e.Response.GetResponseStream()))
                {
                    string responseContent = r.ReadToEnd();
                    errorMessage = Errors.listError[responseContent];
                    errorCode = responseContent;
                    Console.WriteLine(errorMessage);
                }
               
            }

            return nhanVien;

        }
        public bool insertNhanVien(NhanVien nhanVien)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                string res = client.UploadString(url, "POST", JsonConvert.SerializeObject(nhanVien));
                return true;
            }
            catch (WebException e)
            {
                using (StreamReader r = new StreamReader(
                    e.Response.GetResponseStream()))
                {
                    string responseContent = r.ReadToEnd();
                    errorMessage = Errors.listError[responseContent];
                    errorCode = responseContent;
                    Console.WriteLine(errorMessage);
                    return false;
                }
            }

        }
        public bool deleteNhanVien(String id)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                string res = client.UploadString(url + "/" + string.Format(id), "DELETE","");
                return true;
            }
            catch (WebException e)
            {
                using (StreamReader r = new StreamReader(
                     e.Response.GetResponseStream()))
                {
                    string responseContent = r.ReadToEnd();
                    errorMessage = Errors.listError[responseContent];
                    errorCode = responseContent;
                    Console.WriteLine(errorMessage);
                    return false;
                }

            }
        }



        public bool updateNhanVien(NhanVien nhanVien)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                string res = client.UploadString(url, "PUT", JsonConvert.SerializeObject(nhanVien));
                return true;
            }
            catch (WebException e)
            {
                using (StreamReader r = new StreamReader(
                    e.Response.GetResponseStream()))
                {
                    string responseContent = r.ReadToEnd();
                    errorMessage = Errors.listError[responseContent];
                    errorCode = responseContent;
                    Console.WriteLine(errorMessage);
                    return false;
                }
            }
        }
    }
}
