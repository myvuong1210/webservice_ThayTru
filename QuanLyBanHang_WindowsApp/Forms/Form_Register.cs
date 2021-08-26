using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using DevExpress.XtraEditors;
using QLBH_API.Entity;
using QLBH_API.Forms;
using QLBH_API.Services;
using QLBH_API.Validation;

namespace QLBH_API.Forms
{
    public partial class frmTaoTK : DevExpress.XtraEditors.XtraForm
    {
        BindingList<KhachHang> listKhachHang;
        bool isEdit;
        KhachHang khachHangEdit;
        public frmTaoTK()
        {
            InitializeComponent();
        }

        private void frmTaoTK_Load(object sender, EventArgs e)
        {

        }

        private bool checkInfoDangKi()
        {
            if (txtLoginName.Text.Trim().Equals(""))
            {
                MessageBox.Show("Tên dăng nhập không được để trống !", "", MessageBoxButtons.OK);
                txtLoginName.Focus();
                return false;
            }
            if (txtPass.Text.Trim().Equals(""))
            {
                MessageBox.Show("Mật khẩu không được để trống !", "", MessageBoxButtons.OK);
                txtPass.Focus();
                return false;
            }
            if (txtPass.Text.Trim().Equals(""))
            {
                MessageBox.Show("Mật khẩu không được để trống !", "", MessageBoxButtons.OK);
                txtPass.Focus();
                return false;
            }
            return true;
        }

        private void btnTaoTK_Click(object sender, EventArgs e)
        {
            isEdit = false;

            List<KhachHang> khachHangs = new Service_KhachHang().getListKhachHang();
            if (khachHangs.Count > 0) txtLoginName.Text = Program.generateID(khachHangs[khachHangs.Count - 1].id);
            else txtLoginName.Text = "KH001";



            khachHangEdit = new KhachHang();
            khachHangEdit.id = txtLoginName.Text;
            khachHangEdit.hoTen = textTen.Text;
            khachHangEdit.matKhau = txtPass.Text;
            khachHangEdit.sdt = txtSdt.Text;
            khachHangEdit.email = textEmail.Text;
            khachHangEdit.diaChi = richDiaChi.Text;

            if (textEmail.Text.Trim().Length == 0 || !ValidationData.checkEmail(textEmail.Text.Trim()))
            {
                MessageBox.Show("Địa chỉ email trống hoặc không đúng định dạng", "Thông báo", MessageBoxButtons.OK);
                return;
            }
            if (textTen.Text.Length == 0)
            {
                MessageBox.Show("Không để trống để trống họ tên", "Thông báo", MessageBoxButtons.OK);
                return;
            }
            if (txtPass.Text.Length == 0)
            {
                MessageBox.Show("Không để trống để trống mật khẩu", "Thông báo", MessageBoxButtons.OK);
                return;
            }
            if (txtSdt.Text.Trim().Length == 0 || !ValidationData.checkSDT(txtSdt.Text.Trim()))
            {
                MessageBox.Show("Số điện thoại trống hoặc không hợp lệ", "Thông báo", MessageBoxButtons.OK);

                return;
            }
            if (richDiaChi.Text.Length == 0)
            {
                MessageBox.Show("Không để trống để trống địa chỉ", "Thông bá", MessageBoxButtons.OK);
                return;
            }

            if (!isEdit)
            {
                if (textEmail.Focused)
                {
                    khachHangEdit.email = textEmail.Text.Trim();

                }
                if (textTen.Focused)
                {
                    khachHangEdit.hoTen = textTen.Text.Trim();
                }
                if (txtPass.Focused)
                {
                    khachHangEdit.matKhau = txtPass.Text;
                }
                if (txtSdt.Focused)
                {
                    khachHangEdit.sdt = txtSdt.Text.Trim();
                }
                if (richDiaChi.Focused)
                {
                    khachHangEdit.diaChi = richDiaChi.Text.Trim();
                }

            }

            if (!new Service_KhachHang().insertKhachHang(khachHangEdit))
            {
                MessageBox.Show("Đã có lỗi xãy ra! Vui lòng thử lại sau");

                return;
            }

            MessageBox.Show("Thêm Tài Khoản Thành Công! Vui lòng đăng nhập để sử dụng! ");

        }

        private void btnThoat_Click(object sender, EventArgs e)
        {
            DialogResult dr = XtraMessageBox.Show("Bạn thật sự muốn hủy thao tác đăng ký tài khoản?",
                      "Xác thực", MessageBoxButtons.YesNo);

            if (dr == DialogResult.No)
            {
                return;
            }
            else if (dr == DialogResult.Yes)
            {
                this.Close();

            }
        }

        private void lblHintPass_MouseEnter(object sender, EventArgs e)
        {
            txtPass.Properties.UseSystemPasswordChar = false;
        }

        private void txtLoginName_EditValueChanged(object sender, EventArgs e)
        {

        }

        private void txtLoginName_EditValueChanged_1(object sender, EventArgs e)
        {

        }

        

        private void cbShow_CheckedChanged(object sender, EventArgs e)
        {
            txtPass.Properties.UseSystemPasswordChar = (cbShow.Checked) ? false : true;
           
        }
    }
}