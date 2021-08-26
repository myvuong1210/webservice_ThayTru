using DevExpress.XtraEditors;
using QLBH_API.Entity;
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
    public partial class Form_NhanVien : DevExpress.XtraEditors.XtraForm
    {
        bool isEdit = false;
        BindingList<NhanVien> listNhanVien;
        NhanVien NhanVienEdit = null;
        public Form_NhanVien()
        {
            
            InitializeComponent();
            
        }

        private void Form_NhanVien_Load(object sender, EventArgs e)
        {
            // load list

            loadToTable();

            gbTTNV.Enabled = false;

            barButtonItem_Them.Enabled = true;
            barButtonItem_Xoa.Enabled = true;
            barButtonItem_Sua.Enabled = true;
            barButtonItem_Ghi.Enabled = false;
            barButtonItem_Thoat.Enabled = false;

        }
        public void loadToTable()
        {
            List<NhanVien> nhanViens = new Service_NhanVien().getListNhanVien();
            if (listNhanVien == null) listNhanVien = new BindingList<NhanVien>();
            foreach (NhanVien nhanVien in nhanViens)
            {
               listNhanVien.Add(nhanVien);
            }

            //MessageBox.Show(gridControl_NhanVien.DataSource.ToString());
            gcNhanVien.DataSource = listNhanVien;
            gvNhanVien.PopulateColumns();

            gvNhanVien.Columns["id"].Caption = "Mã nhân viên";
            gvNhanVien.Columns["diaChi"].Caption = "Địa chỉ";
            gvNhanVien.Columns["email"].Caption = "Email";
            gvNhanVien.Columns["sdt"].Caption = "Số điện thoại";
            gvNhanVien.Columns["hoTen"].Caption = "Họ tên";
            gvNhanVien.Columns["matKhau"].Caption = "Mật khẩu";
            gvNhanVien.Columns["quyen"].Caption = "Quyền";

            gvNhanVien.Columns["id"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["diaChi"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["email"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["sdt"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["hoTen"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["matKhau"].OptionsColumn.AllowEdit = false;
            gvNhanVien.Columns["quyen"].OptionsColumn.AllowEdit = false;

            gvNhanVien.Columns["matKhau"].Visible = false;
        }

        private void gridView1_FocusedRowChanged(object sender, DevExpress.XtraGrid.Views.Base.FocusedRowChangedEventArgs e)
        {
            if (NhanVienEdit != null) return;
            NhanVien nhanVien = (NhanVien) gvNhanVien.GetFocusedRow();

            if (nhanVien == null) return;
            txtId.Text = nhanVien.id;
            richDiaChi.Text = nhanVien.diaChi;
            txtEmail.Text = nhanVien.email;
            txtTen.Text = nhanVien.hoTen;
            if (nhanVien.quyen) cmbQuyen.SelectedIndex = 1;
            else cmbQuyen.SelectedIndex = 0;
            txtSdt.Text = nhanVien.sdt;
            txtMK.Text = "";
            if (nhanVien.matKhau != null)
            for (int i = 0; i < nhanVien.matKhau.Length; i++)
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

            gcNhanVien.Enabled = false;
           
            gbTTNV.Enabled = true;
            gvNhanVien.AddNewRow();

            List<NhanVien> nhanViens = new Service_NhanVien().getListNhanVien();
            if (nhanViens.Count > 0) txtId.Text = Program.generateID(nhanViens[nhanViens.Count - 1].id);
            else txtId.Text = "NV001";

            gbTTNV.Enabled = true;
            txtMK.Enabled = true;

            txtMK.PasswordChar = '*';


            NhanVienEdit = new NhanVien();
            NhanVienEdit.id = txtId.Text;
            NhanVienEdit.quyen = false;


            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["id"], txtId.Text);
            txtTen.Focus();
        }
       
        private void textBox_SoDienThoai_Leave(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.sdt = txtSdt.Text;
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["sdt"], txtSdt.Text);
        }

        private void textBox_HoTen_Leave(object sender, EventArgs e)
        {
            if(isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.hoTen = txtTen.Text;
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["hoTen"], txtTen.Text);
        }

        private void textBox_Email_Leave(object sender, EventArgs e)
        {
            if(isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.email = txtEmail.Text;
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["email"], txtEmail.Text);
        }

        private void textBox_Password_Leave(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.matKhau = txtMK.Text;
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["matKhau"], txtMK.Text);
        }

        private void richTextBox_DiaChi_Leave(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.diaChi = richDiaChi.Text;
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["diaChi"], richDiaChi.Text);
        }

        private void comboBox_Quyen_Leave(object sender, EventArgs e)
        {
            if (isEdit) return;
            if (NhanVienEdit == null) return;
            NhanVienEdit.quyen = (cmbQuyen.SelectedIndex == 1);
            gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["quyen"], cmbQuyen.SelectedIndex == 1);
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
                MessageBox.Show("Không để trống để trống địa chỉ", "Thông báo", MessageBoxButtons.OK);
                return;
            }


            if (!isEdit)
            {
                if (txtEmail.Focused)
                {
                    NhanVienEdit.email = txtEmail.Text.Trim();
                    gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["email"], txtEmail.Text.Trim());

                }
                if (txtTen.Focused)
                {
                    NhanVienEdit.hoTen = txtTen.Text.Trim();
                    gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["hoTen"], txtTen.Text.Trim());
                }
                if (txtMK.Focused)
                {
                    NhanVienEdit.matKhau = txtMK.Text;
                    gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["matKhau"], txtMK.Text);
                }
                if (txtSdt.Focused)
                {
                    NhanVienEdit.sdt = txtSdt.Text.Trim();
                    gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["sdt"], txtSdt.Text.Trim());
                }
                if (richDiaChi.Focused)
                {
                    NhanVienEdit.diaChi = richDiaChi.Text.Trim();
                    gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["diaChi"], richDiaChi.Text.Trim());
                }


                if (!new Service_NhanVien().insertNhanVien(NhanVienEdit))
                {
                    MessageBox.Show("Đã có lỗi xãy ra! Vui lòng thử lại sau");

                    List<NhanVien> nhanViens = new Service_NhanVien().getListNhanVien();
                    if (nhanViens.Count > 0) txtId.Text = Program.generateID(nhanViens[nhanViens.Count - 1].id);
                    else txtId.Text = "NV001";

                    return;
                }
    

                NhanVienEdit = null;
                gvNhanVien.FocusedRowHandle = 0;
            }
            else
            {
                NhanVienEdit.hoTen = txtTen.Text;
                NhanVienEdit.quyen = (cmbQuyen.SelectedIndex == 1);
                NhanVienEdit.diaChi = richDiaChi.Text;
                NhanVienEdit.email = txtEmail.Text;
                NhanVienEdit.sdt = txtSdt.Text;


                if (!new Service_NhanVien().updateNhanVien(NhanVienEdit))
                {
                    MessageBox.Show("Đã có lỗi xãy ra! Vui lòng thử lại sau");
                    return;
                }    

                gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["hoTen"], NhanVienEdit.hoTen);
                gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["quyen"], NhanVienEdit.quyen);
                gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["diaChi"], NhanVienEdit.diaChi);
                gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["email"], NhanVienEdit.email);
                gvNhanVien.SetFocusedRowCellValue(gvNhanVien.Columns["sdt"], NhanVienEdit.sdt);

                NhanVienEdit = null;
            }    

            gbTTNV.Enabled = false;
            gcNhanVien.Enabled = true;
            barButtonItem_Them.Enabled = true;
            barButtonItem_Xoa.Enabled = true;
            barButtonItem_Sua.Enabled = true;
            barButtonItem_Ghi.Enabled = false;
            barButtonItem_Thoat.Enabled = false;
        }

        private void barButtonItem_Xoa_ItemClick(object sender, DevExpress.XtraBars.ItemClickEventArgs e)
        {
            var confirm = MessageBox.Show("Bạn có chắc chắn muốn xóa nhân viên " + txtId.Text + "?", "Xác nhận", MessageBoxButtons.YesNo, MessageBoxIcon.Question);

          
            if (confirm == DialogResult.Yes)
            {
                if (Form_Login.username.ToUpper() == txtId.Text.Trim().ToUpper())
                {
                    MessageBox.Show("Không thể xóa mẫu tin này", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    return;
                }
                   if (new Service_NhanVien().deleteNhanVien(txtId.Text.Trim()))
                    {
                        gvNhanVien.DeleteSelectedRows();
                        if (gvNhanVien.RowCount > 0) gvNhanVien.FocusedRowHandle = 0;
                        return;
                    }
                   
                    if (!(Service_NhanVien.errorCode.Equals("5")))
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

            NhanVienEdit = new NhanVien();
            NhanVienEdit.id = txtId.Text;
            NhanVienEdit.hoTen = txtTen.Text;
            NhanVienEdit.quyen = (cmbQuyen.SelectedIndex == 1);
            NhanVienEdit.diaChi = richDiaChi.Text;
            NhanVienEdit.email = txtEmail.Text;
            NhanVienEdit.sdt = txtSdt.Text;
            NhanVienEdit.matKhau = (string)gvNhanVien.GetFocusedRowCellValue(gvNhanVien.Columns["matKhau"]);

            gcNhanVien.Enabled = false;
            gbTTNV.Enabled = true;
            txtMK.Enabled = false;
        }
    }
}