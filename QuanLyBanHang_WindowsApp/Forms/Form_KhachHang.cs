using DevExpress.XtraEditors;
using QLBH_API.Entity;
using QLBH_API.Forms;
using QLBH_API.Services;
using QLBH_API.Validation;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace QLBH_API.Forms
{
    public partial class Form_KhachHang : DevExpress.XtraEditors.XtraForm
    {
        BindingList<KhachHang> listKhachHang;
        bool isEdit;
        KhachHang khachHangEdit;
        public Form_KhachHang()
        {
            InitializeComponent();
        }

        private void Form_KhachHang_Load(object sender, EventArgs e)
        {
            loadToTable();

            gbTTKH.Enabled = false;
           

            switch (Form_Login.role)
            {
                case 0: // nhân viên
                    {
                        barButtonItem_Them.Enabled = true;
                        barButtonItem_Xoa.Enabled = true;
                        barButtonItem_Sua.Enabled = true;
                        barButtonItem_Ghi.Enabled = false;
                        barButtonItem_Thoat.Enabled = false;
                        break;
                    }
                case 1: // admin
                    {
                        barButtonItem_Them.Enabled = true;
                        barButtonItem_Xoa.Enabled = true;
                        barButtonItem_Sua.Enabled = true;
                        barButtonItem_Ghi.Enabled = false;
                        barButtonItem_Thoat.Enabled = false;
                        break;
                    }
            }
        }
        public void loadToTable()
        {
            List<KhachHang> khachHangs = new Service_KhachHang().getListKhachHang();
            if (listKhachHang == null) listKhachHang = new BindingList<KhachHang>();
            foreach (KhachHang khachHang in khachHangs)
            {
                listKhachHang.Add(khachHang);
            }

            gcKhachHang.DataSource = listKhachHang;
            gvKhachHang.PopulateColumns();

            gvKhachHang.Columns["id"].Caption = "Mã tài khoản";
            gvKhachHang.Columns["diaChi"].Caption = "Địa chỉ";
            gvKhachHang.Columns["email"].Caption = "Email";
            gvKhachHang.Columns["sdt"].Caption = "Số điện thoại";
            gvKhachHang.Columns["hoTen"].Caption = "Họ tên";
            gvKhachHang.Columns["matKhau"].Caption = "Mật khẩu";
            

            gvKhachHang.Columns["id"].OptionsColumn.AllowEdit = false;
            gvKhachHang.Columns["diaChi"].OptionsColumn.AllowEdit = false;
            gvKhachHang.Columns["email"].OptionsColumn.AllowEdit = false;
            gvKhachHang.Columns["sdt"].OptionsColumn.AllowEdit = false;
            gvKhachHang.Columns["hoTen"].OptionsColumn.AllowEdit = false;
            gvKhachHang.Columns["matKhau"].OptionsColumn.AllowEdit = false;
            

            gvKhachHang.Columns["matKhau"].Visible = false;
        }

        private void gvKhachHang_FocusedRowChanged(object sender, DevExpress.XtraGrid.Views.Base.FocusedRowChangedEventArgs e)
        {
            KhachHang khachHang = (KhachHang)gvKhachHang.GetFocusedRow();

            if (khachHang == null) return;
            txtId.Text = khachHang.id;
            richDiaChi.Text = khachHang.diaChi;
            txtEmail.Text = khachHang.email;
            txtTen.Text = khachHang.hoTen;
            txtSdt.Text = khachHang.sdt;
            txtMK.Text = "";
            if (khachHang.matKhau != null)
                for (int i = 0; i < khachHang.matKhau.Length; i++)
                {
                    txtMK.Text += '*';
                }
        }

        private void barButtonItem_Them_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            isEdit = false;
            barButtonItem_Them.Enabled = false;
            barButtonItem_Xoa.Enabled = false;
            barButtonItem_Sua.Enabled = false;
            barButtonItem_Ghi.Enabled = true;
            barButtonItem_Thoat.Enabled = true;
            gcKhachHang.Enabled = false;
            gbTTKH.Enabled = true;

            gvKhachHang.AddNewRow();
            txtMK.PasswordChar = '*';

            List<KhachHang> khachHangs = new Service_KhachHang().getListKhachHang();
            if (khachHangs.Count > 0) txtId.Text = Program.generateID(khachHangs[khachHangs.Count - 1].id);
            else txtId.Text = "KH001";
            gbTTKH.Enabled = true;
            txtMK.Enabled = true;

            khachHangEdit = new KhachHang();
            khachHangEdit.id = txtId.Text;

            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["id"], txtId.Text);
            
            txtTen.Focus();
            
        }
        
        private void barButtonItem_Thoat_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            this.Close();
            return;
        }

        private void barButtonItem_Ghi_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            if (txtEmail.Text.Trim().Length == 0 || !ValidationData.checkEmail(txtEmail.Text.Trim()))
            {
                MessageBox.Show("Địa chỉ email trống hoặc không đúng định dạng", "Thông báo", MessageBoxButtons.OK);
                return;
            }
            if (txtTen.Text.Length == 0)
            {
                MessageBox.Show("Không để trống để trống họ tên", "Thông báo", MessageBoxButtons.OK);
                return;
            }
            if (txtMK.Text.Length == 0)
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
                if (!new Service_KhachHang().insertKhachHang(khachHangEdit))
                {
                    MessageBox.Show("Đã có lỗi xãy ra! Vui lòng thử lại sau");

                    List<KhachHang> khachHangs = new Service_KhachHang().getListKhachHang();
                    if (khachHangs.Count > 0) txtId.Text = Program.generateID(khachHangs[khachHangs.Count - 1].id);
                    else txtId.Text = "KH001";

                    return;
                }


                khachHangEdit = null;
                gvKhachHang.FocusedRowHandle = 0;
            }
            else
            {
                khachHangEdit.hoTen = txtTen.Text;
                khachHangEdit.diaChi = richDiaChi.Text;
                khachHangEdit.email = txtEmail.Text;
                khachHangEdit.sdt = txtSdt.Text;


                if (!new Service_KhachHang().updateKhachHang(khachHangEdit))
                {
                    MessageBox.Show("Đã có lỗi xảy ra! Vui lòng thử lại sau");
                    return;
                }

                gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["hoTen"], khachHangEdit.hoTen);
                gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["diaChi"], khachHangEdit.diaChi);
                gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["email"], khachHangEdit.email);
                gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["sdt"], khachHangEdit.sdt);

                khachHangEdit = null;
            }

            gbTTKH.Enabled = false;
            barButtonItem_Them.Enabled = true;
            barButtonItem_Xoa.Enabled = true;
            barButtonItem_Sua.Enabled = true;
            barButtonItem_Ghi.Enabled = false;
            barButtonItem_Thoat.Enabled = false;
            
            gcKhachHang.Enabled = true;
            gbTTKH.Enabled = false;
        }

        private void barButtonItem_Xoa_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            var confirm = MessageBox.Show("Bạn có chắc muốn xóa khách hàng " + txtId.Text + "?", "Xác nhận", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
            if (confirm == DialogResult.Yes)
            {
                if (new Service_KhachHang().deleteKhachHang(txtId.Text.Trim()))
                {
                    gvKhachHang.DeleteSelectedRows();
                    if (gvKhachHang.RowCount > 0) gvKhachHang.FocusedRowHandle = 0;
                    return;
                }
                
                else if (Service_NhanVien.errorCode.Equals("5"))
                {
                    MessageBox.Show("Không thể xóa mẫu tin này", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    return;
                }
                MessageBox.Show("Lỗi máy chủ, vui lòng thử lại sau", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return;
            }
        }

        private void barButtonItem_Sua_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            isEdit = true;
            barButtonItem_Them.Enabled = false;
            barButtonItem_Xoa.Enabled = false;
            barButtonItem_Sua.Enabled = false;
            barButtonItem_Ghi.Enabled = true;
            barButtonItem_Thoat.Enabled = true;

            khachHangEdit = new KhachHang();
            khachHangEdit.id = txtId.Text;
            khachHangEdit.hoTen = txtTen.Text;
            khachHangEdit.diaChi = richDiaChi.Text;
            khachHangEdit.email = txtEmail.Text;
            khachHangEdit.sdt = txtSdt.Text;
            khachHangEdit.matKhau = (string)gvKhachHang.GetFocusedRowCellValue(gvKhachHang.Columns["matKhau"]);

            gcKhachHang.Enabled = false;
            gbTTKH.Enabled = true;
            txtMK.Enabled = false;
        }

        private void txtSdt_TextChanged(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (khachHangEdit == null) return;
            khachHangEdit.sdt = txtSdt.Text;
            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["sdt"], txtSdt.Text);
         }

        private void txtTen_TextChanged(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (khachHangEdit == null) return;
            khachHangEdit.hoTen = txtTen.Text;
            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["hoTen"], txtTen.Text);
           
        }

        private void txtEmail_TextChanged(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (khachHangEdit == null) return;
            khachHangEdit.email = txtEmail.Text;
            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["email"], txtEmail.Text);
            

        }

        private void txtMK_TextChanged(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (khachHangEdit == null) return;
            khachHangEdit.matKhau = txtMK.Text;
            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["matKhau"], txtMK.Text);
           }

        private void richDiaChi_TextChanged(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (khachHangEdit == null) return;
            khachHangEdit.diaChi =richDiaChi.Text;
            gvKhachHang.SetFocusedRowCellValue(gvKhachHang.Columns["diaChi"], richDiaChi.Text);
        }
    }
}