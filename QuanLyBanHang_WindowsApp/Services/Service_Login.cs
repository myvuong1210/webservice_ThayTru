using QLBH_API.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Net.Http;
using System.Net.Http.Headers;
namespace QLBH_API.Services
{
    class Service_Login
    {
        public int login(Login login)
        { 
           string url = Program.baseURL + string.Format("login");
            WebClient wc = new WebClient();
            wc.Encoding = System.Text.Encoding.UTF8;
            wc.Headers[HttpRequestHeader.ContentType] = "application/json";

            String kq = wc.UploadString(url, "POST", JsonConvert.SerializeObject(login));

            return int.Parse(kq);
        }
    }
}
