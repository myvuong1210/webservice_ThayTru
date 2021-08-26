using DevExpress.XtraEditors;
using QLBH_API.Entity;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using QLBH_API.Services;

namespace QLBH_API.Forms
{
    public partial class Form_Login : DevExpress.XtraEditors.XtraForm
    {
        public static string username;
        public static string password;
        public static int role = -1;
        public Form_Login()
        {
            InitializeComponent();
            textBox_password.PasswordChar = '*';
        }

        private void textBox_ID_TextChanged(object sender, EventArgs e)
        {

        }

        private void button_DangNhap_Click(object sender, EventArgs e)
        {
            username = textBox_ID.Text;
            password = textBox_password.Text;

            Login login = new Login();
            login.username = username;
            login.password = password;

            int role = new Service_Login().login(login);


            switch (role)
            {
                case -1:
                    {
                        MessageBox.Show("Sai thông tin đăng nhập", "Thông báo", MessageBoxButtons.OK);
                    
                        break;
                    }
                case 0: // nhân viên
                    {
                        Form_Login.role = 0;
                        Program.mGroup = "NhanVien";
                        Program.frmChinh = new frm_Main();
                        Program.frmChinh.Show();
                     
                        break;
                    }
                case 1: // admin
                    {
                        Form_Login.role = 1;
                        Program.mGroup = "Admin";
                        Program.frmChinh = new frm_Main();
                        Program.frmChinh.Show();
                       
                        break;
                    }
                case 2: // khách hàng
                    {
                        Form_Login.role = 2;
                        Program.mGroup = "KhachHang";
                        Program.frmChinh = new frm_Main();
                        Program.frmChinh.Show();
                      
                        break;
                    }

            }

           
        }

        private void button1_Click(object sender, EventArgs e)
        {

        }

        private void txtPassword_TextChanged(object sender, EventArgs e)
        {

        }

        private void txtLogin_TextChanged(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            Program.FrmTaoTK = new frmTaoTK();
            Program.FrmTaoTK.Show();
        }
    }
}