namespace binaes
{
    partial class formContacto
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.panelTopContacto = new System.Windows.Forms.Panel();
            this.lblContactanos = new System.Windows.Forms.Label();
            this.lblContactosDirectos = new System.Windows.Forms.Label();
            this.panelContactosContent = new System.Windows.Forms.Panel();
            this.lblDireccion = new System.Windows.Forms.Label();
            this.lblEscribenos = new System.Windows.Forms.Label();
            this.lblLlamanos = new System.Windows.Forms.Label();
            this.panelTopContacto.SuspendLayout();
            this.panelContactosContent.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelTopContacto
            // 
            this.panelTopContacto.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panelTopContacto.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(46)))), ((int)(((byte)(129)))));
            this.panelTopContacto.Controls.Add(this.lblContactanos);
            this.panelTopContacto.Location = new System.Drawing.Point(0, 0);
            this.panelTopContacto.Name = "panelTopContacto";
            this.panelTopContacto.Size = new System.Drawing.Size(779, 90);
            this.panelTopContacto.TabIndex = 0;
            // 
            // lblContactanos
            // 
            this.lblContactanos.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.lblContactanos.AutoSize = true;
            this.lblContactanos.Font = new System.Drawing.Font("Malgun Gothic", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblContactanos.ForeColor = System.Drawing.SystemColors.Window;
            this.lblContactanos.Location = new System.Drawing.Point(276, 25);
            this.lblContactanos.Name = "lblContactanos";
            this.lblContactanos.Size = new System.Drawing.Size(189, 40);
            this.lblContactanos.TabIndex = 1;
            this.lblContactanos.Text = "Contáctanos";
            // 
            // lblContactosDirectos
            // 
            this.lblContactosDirectos.AutoSize = true;
            this.lblContactosDirectos.Font = new System.Drawing.Font("Malgun Gothic", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblContactosDirectos.Location = new System.Drawing.Point(238, 140);
            this.lblContactosDirectos.Name = "lblContactosDirectos";
            this.lblContactosDirectos.Size = new System.Drawing.Size(274, 40);
            this.lblContactosDirectos.TabIndex = 1;
            this.lblContactosDirectos.Text = "Contactos directos";
            // 
            // panelContactosContent
            // 
            this.panelContactosContent.Controls.Add(this.lblLlamanos);
            this.panelContactosContent.Controls.Add(this.lblEscribenos);
            this.panelContactosContent.Controls.Add(this.lblDireccion);
            this.panelContactosContent.Location = new System.Drawing.Point(86, 209);
            this.panelContactosContent.Name = "panelContactosContent";
            this.panelContactosContent.Size = new System.Drawing.Size(602, 218);
            this.panelContactosContent.TabIndex = 2;
            // 
            // lblDireccion
            // 
            this.lblDireccion.Font = new System.Drawing.Font("Malgun Gothic", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblDireccion.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(106)))), ((int)(((byte)(106)))), ((int)(((byte)(106)))));
            this.lblDireccion.Location = new System.Drawing.Point(10, 9);
            this.lblDireccion.Name = "lblDireccion";
            this.lblDireccion.Size = new System.Drawing.Size(589, 62);
            this.lblDireccion.TabIndex = 0;
            this.lblDireccion.Text = "Dirección: Alameda Doctor Manuel Enrique Araujo No 5500, San \r\nSalvador, El Salva" +
    "dor, C.A.\r\n\r\n\r\n";
            // 
            // lblEscribenos
            // 
            this.lblEscribenos.Font = new System.Drawing.Font("Malgun Gothic", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblEscribenos.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(106)))), ((int)(((byte)(106)))), ((int)(((byte)(106)))));
            this.lblEscribenos.Location = new System.Drawing.Point(10, 80);
            this.lblEscribenos.Name = "lblEscribenos";
            this.lblEscribenos.Size = new System.Drawing.Size(385, 39);
            this.lblEscribenos.TabIndex = 1;
            this.lblEscribenos.Text = "Escríbenos: contacto@presidencia.gob.sv";
            // 
            // lblLlamanos
            // 
            this.lblLlamanos.Font = new System.Drawing.Font("Malgun Gothic", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblLlamanos.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(106)))), ((int)(((byte)(106)))), ((int)(((byte)(106)))));
            this.lblLlamanos.Location = new System.Drawing.Point(10, 132);
            this.lblLlamanos.Name = "lblLlamanos";
            this.lblLlamanos.Size = new System.Drawing.Size(385, 39);
            this.lblLlamanos.TabIndex = 2;
            this.lblLlamanos.Text = "Llámanos: (503) 2248-9000";
            // 
            // formContacto
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(779, 500);
            this.Controls.Add(this.panelContactosContent);
            this.Controls.Add(this.lblContactosDirectos);
            this.Controls.Add(this.panelTopContacto);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Name = "formContacto";
            this.Text = "Form1";
            this.panelTopContacto.ResumeLayout(false);
            this.panelTopContacto.PerformLayout();
            this.panelContactosContent.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Panel panelTopContacto;
        private Label lblContactanos;
        private Label lblContactosDirectos;
        private Panel panelContactosContent;
        private Label lblLlamanos;
        private Label lblEscribenos;
        private Label lblDireccion;
    }
}