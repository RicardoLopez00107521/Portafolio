namespace binaes
{
    partial class formAreaBiblioteca
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
            this.panelTitle = new System.Windows.Forms.Panel();
            this.lblTitle = new System.Windows.Forms.Label();
            this.tabBiblioteca = new System.Windows.Forms.TabControl();
            this.tabRegistros = new System.Windows.Forms.TabPage();
            this.tabBuscador = new System.Windows.Forms.TabPage();
            this.tabServiciosBibliotecarios = new System.Windows.Forms.TabPage();
            this.panelTitle.SuspendLayout();
            this.tabBiblioteca.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelTitle
            // 
            this.panelTitle.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panelTitle.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(46)))), ((int)(((byte)(129)))));
            this.panelTitle.Controls.Add(this.lblTitle);
            this.panelTitle.Location = new System.Drawing.Point(0, 0);
            this.panelTitle.Name = "panelTitle";
            this.panelTitle.Size = new System.Drawing.Size(779, 90);
            this.panelTitle.TabIndex = 0;
            // 
            // lblTitle
            // 
            this.lblTitle.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Malgun Gothic", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblTitle.ForeColor = System.Drawing.SystemColors.Window;
            this.lblTitle.Location = new System.Drawing.Point(250, 23);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(269, 40);
            this.lblTitle.TabIndex = 1;
            this.lblTitle.Text = "Área de biblioteca";
            this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // tabBiblioteca
            // 
            this.tabBiblioteca.Controls.Add(this.tabRegistros);
            this.tabBiblioteca.Controls.Add(this.tabBuscador);
            this.tabBiblioteca.Controls.Add(this.tabServiciosBibliotecarios);
            this.tabBiblioteca.Location = new System.Drawing.Point(31, 96);
            this.tabBiblioteca.Name = "tabBiblioteca";
            this.tabBiblioteca.SelectedIndex = 0;
            this.tabBiblioteca.Size = new System.Drawing.Size(720, 379);
            this.tabBiblioteca.TabIndex = 1;
            // 
            // tabRegistros
            // 
            this.tabRegistros.Location = new System.Drawing.Point(4, 24);
            this.tabRegistros.Name = "tabRegistros";
            this.tabRegistros.Padding = new System.Windows.Forms.Padding(3);
            this.tabRegistros.Size = new System.Drawing.Size(712, 351);
            this.tabRegistros.TabIndex = 0;
            this.tabRegistros.Text = "Registros";
            this.tabRegistros.UseVisualStyleBackColor = true;
            // 
            // tabBuscador
            // 
            this.tabBuscador.Location = new System.Drawing.Point(4, 24);
            this.tabBuscador.Name = "tabBuscador";
            this.tabBuscador.Padding = new System.Windows.Forms.Padding(3);
            this.tabBuscador.Size = new System.Drawing.Size(712, 351);
            this.tabBuscador.TabIndex = 1;
            this.tabBuscador.Text = "Buscador";
            this.tabBuscador.UseVisualStyleBackColor = true;
            // 
            // tabServiciosBibliotecarios
            // 
            this.tabServiciosBibliotecarios.Location = new System.Drawing.Point(4, 24);
            this.tabServiciosBibliotecarios.Name = "tabServiciosBibliotecarios";
            this.tabServiciosBibliotecarios.Padding = new System.Windows.Forms.Padding(3);
            this.tabServiciosBibliotecarios.Size = new System.Drawing.Size(712, 351);
            this.tabServiciosBibliotecarios.TabIndex = 2;
            this.tabServiciosBibliotecarios.Text = "Servicios Bibliotecarios";
            this.tabServiciosBibliotecarios.UseVisualStyleBackColor = true;
            // 
            // formAreaBiblioteca
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(779, 500);
            this.Controls.Add(this.tabBiblioteca);
            this.Controls.Add(this.panelTitle);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Name = "formAreaBiblioteca";
            this.Text = "Form1";
            this.panelTitle.ResumeLayout(false);
            this.panelTitle.PerformLayout();
            this.tabBiblioteca.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private Panel panelTitle;
        public Label lblTitle;
        private TabControl tabBiblioteca;
        private TabPage tabRegistros;
        private TabPage tabBuscador;
        private TabPage tabServiciosBibliotecarios;
    }
}