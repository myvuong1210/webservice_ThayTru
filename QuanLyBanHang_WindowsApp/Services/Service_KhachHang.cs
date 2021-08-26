using Newtonsoft.Json;
using QLBH_API.Entity;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace QLBH_API.Services
{
    class Service_KhachHang
    {
        public static string errorCode;
        public static string errorMessage;
        string url = Program.baseURL + string.Format("khachhang");
        public List<KhachHang> getListKhachHang()
        {
            List<KhachHang> khachHangs = null;
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            
            try
            {
                string data = client.DownloadString(url);
                khachHangs = JsonConvert.DeserializeObject<List<KhachHang>>(data);
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
            return khachHangs;
        }
        public bool insertKhachHang(KhachHang khachHang)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                client.UploadString(url, "POST", JsonConvert.SerializeObject(khachHang));
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
                }
                return false;
            }
        }


        public bool deleteKhachHang(string id)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                client.UploadString(url + string.Format("/" + id), "DELETE", "");
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

        public bool updateKhachHang(KhachHang khachHang)
        {
            WebClient client = new WebClient();
            client.Encoding = System.Text.Encoding.UTF8;
            client.Headers[HttpRequestHeader.ContentType] = "application/json";

            try
            {
                client.UploadString(url, "PUT", JsonConvert.SerializeObject(khachHang));
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
                }
                return false;
            }
        }
    }
}
