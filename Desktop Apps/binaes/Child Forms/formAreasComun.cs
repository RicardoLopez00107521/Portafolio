using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;

namespace binaes
{
    public partial class formAreasComun : Form
    {
        

        public int IdArea;

        public formAreasComun()
        {
            InitializeComponent();

            switch (lblTitle.Text)
            {
                case "Salones Lúdicos": 
                    IdArea = 1;
                    break;
            }
        }

        private void btnGetTime_Click(object sender, EventArgs e)
        {
            txtHoraEntrada.Text = DateTime.Now.ToShortTimeString();
        }

        private void btnGetTimeSalida_Click(object sender, EventArgs e)
        {
            txtHoraSalida.Text = DateTime.Now.ToShortTimeString();
        }

        private void btnRegEntrada_Click(object sender, EventArgs e)
        {
            Ingreso ing = new Ingreso();
            ing.areaID = Convert.ToInt32(txtAreaIdeEntrada.Text);
            ing.codigoUsuario = Convert.ToInt32(txtUserEntrada.Text);
            ing.fechaEntrada = txtFechaEntrada.Text;
            ing.horaEntrada = txtHoraEntrada.Text;
            IngresoADO.Insertar(ing);
        }

        private void btnRegistrarSalida_Click(object sender, EventArgs e)
        {
            Abandono aban = new Abandono();
            aban.areaID = Convert.ToInt32(txtAreaIdSalida.Text);
            aban.codigoUsuario = Convert.ToInt32(txtUserSalida.Text);
            aban.fechaSalida = txtFechaSalida.Text;
            aban.horaSalida = txtHoraSalida.Text;
            AbandonoADO.Insertar(aban);
        }
    }
}
