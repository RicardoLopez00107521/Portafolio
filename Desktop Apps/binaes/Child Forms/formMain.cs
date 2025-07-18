using binaes.Properties;

namespace binaes
{
    public partial class formMain : Form
    {
        private List<Bitmap> listaImagenes;

        public formMain()
        {
            InitializeComponent();

            customizeDesing();

            listaImagenes = new List<Bitmap>();
            listaImagenes.Add(Resources.banner01);
            listaImagenes.Add(Resources.banner2);
            listaImagenes.Add(Resources.banner3);
            listaImagenes.Add(Resources.banner4);
        }

        private int contadorBanner = 0;

        private void tmrInicioBanner_Tick(object sender, EventArgs e)
        {
            if (contadorBanner < listaImagenes.Count)
            {
                picBannerInicio.Image = listaImagenes[contadorBanner];
                contadorBanner++;

                if (contadorBanner == listaImagenes.Count)
                {
                    contadorBanner = 0;
                }
            }
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
            panelChildForm.Controls.Add(childForm);
            panelChildForm.Tag = childForm;
            childForm.BringToFront();
            childForm.Show();
        }

        private void customizeDesing()
        {
            panelSubmenuAreas.Visible = false;
        }
        private void hideSubmenu()
        {
            if(panelSubmenuAreas.Visible == true)
                panelSubmenuAreas.Visible=false;
        }
        private void showSubmenu(Panel subMenu)
        {
            if (subMenu.Visible == false)
            {
                hideSubmenu();
                subMenu.Visible = true;
            }
            else
            {
                subMenu.Visible = false;
            }
        }

        private void btnInicio_Click(object sender, EventArgs e)
        {
            if(activeForm != null)
                activeForm.Close();
        }

        private void btnAreas_Click(object sender, EventArgs e)
        {
            showSubmenu(panelSubmenuAreas);

        }

        private void btnContacto_Click(object sender, EventArgs e)
        {
            openChildForm(new formContacto());
        }

        private void btnAdministracion_Click(object sender, EventArgs e)
        {
            openChildForm(new formAdminLog());
        }

        private void btnSalonesLudicos_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Salones Lúdicos";
            openChildForm(formAreasComun);
        }

        private void btnAuditorium_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Auditórium";
            openChildForm(formAreasComun);
        }

        private void btnSalaProyeccion_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Sala de proyección";
            openChildForm(formAreasComun);
        }

        private void btnAreaComputacion_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Área de Computación";
            openChildForm(formAreasComun);
        }

        private void btnAreaInclusion_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Área de inclusión";
            openChildForm(formAreasComun);
        }

        private void btnSalaInvestigacion_Click(object sender, EventArgs e)
        {
            formAreasComun formAreasComun = new formAreasComun();
            formAreasComun.lblTitle.Text = "Sala de Investigación";
            openChildForm(formAreasComun);
        }

        private void btnAreaBiblioteca_Click(object sender, EventArgs e)
        {
            if (activeForm != null)
                activeForm.Close();
            openChildForm(new formAreaBiblioteca());
        }
    }
}