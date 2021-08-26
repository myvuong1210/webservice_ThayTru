using DevExpress.XtraBars;
using DevExpress.XtraReports.UI;
using QLBH_API.Forms;
using System;
using System.Windows.Forms;
namespace QLBH_API
{
    public partial class frm_Main : DevExpress.XtraBars.Ribbon.RibbonForm
    {
        public frm_Main()
        {
            InitializeComponent();
        }

        //Kien tra form da load vao bo nho chua ?
        private Form CheckExists(Type ftype)
        {
            foreach (Form f in this.MdiChildren)
                if (f.GetType() == ftype)
                    return f;
            return null;
        }

        private void Form1_Load(object sender, EventArgs e)
        {

            if (Program.mGroup == "NhanVien")// PKT
            {
                this.barBtnKhachHang.Enabled = true;
                this.barBtnHangHoa.Enabled = true;
                this.barBtnNhanVien.Enabled = false;
                this.barBtnNhapHang.Enabled = true;
                this.barBtnDonDatHang.Enabled = true;
                this.barBtn_Dangxuat.Enabled = true;
            }
            else if (Program.mGroup == "Admin")// PKT
            {
                this.barBtnKhachHang.Enabled = true;
                this.barBtnHangHoa.Enabled = true;
                this.barBtnNhanVien.Enabled = true;
                this.barBtnNhapHang.Enabled = true;
                this.barBtnDonDatHang.Enabled = true;
                this.barBtn_Dangxuat.Enabled = true;
            }
            else if (Program.mGroup == "KhachHang")// PKT
            {
                this.barBtnHangHoa.Enabled = true;
                this.barBtnDonDatHang.Enabled = true;
                this.barBtnNhanVien.Enabled = false;
                this.barBtnKhachHang.Enabled = false;
                this.barBtnNhapHang.Enabled = false;
                this.barBtnDonDatHang.Enabled = true;
                this.barBtn_Dangxuat.Enabled = true;

            }


            }

    


        private void barDangXuat_ItemClick(object sender, ItemClickEventArgs e)
        {
            frm_Main n = new frm_Main();
            n.Dispose(false);
            Program.frmChinh.Close();
            Program.FrmLogin.textBox_ID.Text = "";
            Program.FrmLogin.textBox_password.Text = "";
            Form_Login.username = "";
            Form_Login.password = "";

        }

        private void barBtnKhachHang_ItemClick(object sender, ItemClickEventArgs e)
        {
            Form frm = this.CheckExists(typeof(Form_KhachHang));
            if (frm != null) frm.Activate();
            else
            {
                Form_KhachHang f = new Form_KhachHang();
                f.MdiParent = this;
                f.Show();
            }
        }

        private void barBtnNhanVien_ItemClick(object sender, ItemClickEventArgs e)
        {
            Form frm = this.CheckExists(typeof(Form_NhanVien));
            if (frm != null) frm.Activate();
            else
            {
                Form_NhanVien f = new Form_NhanVien();
                f.MdiParent = this;
                f.Show();
            }
        }

        private void barBtnHangHoa_ItemClick(object sender, ItemClickEventArgs e)
        {
            
        }

        private void barBtnNhapHang_ItemClick(object sender, ItemClickEventArgs e)
        {
           
        }

        private void barBtnDonDatHang_ItemClick(object sender, ItemClickEventArgs e)
        {
           
        }

     }
}
