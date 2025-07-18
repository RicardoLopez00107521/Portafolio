using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace binaes
{
    public partial class formAdminLog : Form
    {
        public formAdminLog()
        {
            InitializeComponent();
        }

        private Form activeForm;

        private void openChildForm(Form childForm)
        {
            if (activeForm != null)
            {
                activeForm.Close();
            }
            activeForm = childForm;
            childForm.TopLevel = false;
            childForm.FormBorderStyle = FormBorderStyle.None;
            childForm.Dock = DockStyle.Fill;
            panelChildFormAdmin.Controls.Add(childForm);
            panelChildFormAdmin.Tag = childForm;
            childForm.BringToFront();
            childForm.Show();
        }

        private void btnIngresar_Click(object sender, EventArgs e)
        {
            if (txtPassword.Text == "12345")
                openChildForm(new formAdminMenu());
            else
            {
                string mensaje = "La contraseña ingresada es incorrecta, intentelo nuevamente";
                MessageBox.Show(mensaje, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
