namespace binaes
{
    partial class formAdminLog
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
            this.panelChildFormAdmin = new System.Windows.Forms.Panel();
            this.btnIngresar = new System.Windows.Forms.Button();
            this.txtPassword = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.lblInformacion = new System.Windows.Forms.Label();
            this.lblWarning = new System.Windows.Forms.Label();
            this.panelTopTitle = new System.Windows.Forms.Panel();
            this.lblTitle = new System.Windows.Forms.Label();
            this.panelChildFormAdmin.SuspendLayout();
            this.panelTopTitle.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelChildFormAdmin
            // 
            this.panelChildFormAdmin.Controls.Add(this.btnIngresar);
            this.panelChildFormAdmin.Controls.Add(this.txtPassword);
            this.panelChildFormAdmin.Controls.Add(this.label2);
            this.panelChildFormAdmin.Controls.Add(this.lblInformacion);
            this.panelChildFormAdmin.Controls.Add(this.lblWarning);
            this.panelChildFormAdmin.Controls.Add(this.panelTopTitle);
            this.panelChildFormAdmin.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelChildFormAdmin.Location = new System.Drawing.Point(0, 0);
            this.panelChildFormAdmin.Name = "panelChildFormAdmin";
            this.panelChildFormAdmin.Size = new System.Drawing.Size(763, 461);
            this.panelChildFormAdmin.TabIndex = 10;
            // 
            // btnIngresar
            // 
            this.btnIngresar.Location = new System.Drawing.Point(322, 299);
            this.btnIngresar.Name = "btnIngresar";
            this.btnIngresar.Size = new System.Drawing.Size(75, 23);
            this.btnIngresar.TabIndex = 5;
            this.btnIngresar.Text = "Ingresar";
            this.btnIngresar.UseVisualStyleBackColor = true;
            this.btnIngresar.Click += new System.EventHandler(this.btnIngresar_Click);
            // 
            // txtPassword
            // 
            this.txtPassword.Location = new System.Drawing.Point(251, 260);
            this.txtPassword.Name = "txtPassword";
            this.txtPassword.Size = new System.Drawing.Size(233, 23);
            this.txtPassword.TabIndex = 4;
            this.txtPassword.UseSystemPasswordChar = true;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Malgun Gothic", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.label2.Location = new System.Drawing.Point(161, 262);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(84, 17);
            this.label2.TabIndex = 3;
            this.label2.Text = "Contraseña: ";
            // 
            // lblInformacion
            // 
            this.lblInformacion.AutoSize = true;
            this.lblInformacion.Font = new System.Drawing.Font("Malgun Gothic", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point);
            this.lblInformacion.Location = new System.Drawing.Point(94, 178);
            this.lblInformacion.Name = "lblInformacion";
            this.lblInformacion.Size = new System.Drawing.Size(548, 17);
            this.lblInformacion.TabIndex = 2;
            this.lblInformacion.Text = "Para poder ingresar a esta zona deberás proporcionar una contraseña de administra" +
    "dor";
            // 
            // lblWarning
            // 
            this.lblWarning.AutoSize = true;
            this.lblWarning.Font = new System.Drawing.Font("Malgun Gothic", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblWarning.ForeColor = System.Drawing.Color.Red;
            this.lblWarning.Location = new System.Drawing.Point(284, 119);
            this.lblWarning.Name = "lblWarning";
            this.lblWarning.Size = new System.Drawing.Size(170, 40);
            this.lblWarning.TabIndex = 1;
            this.lblWarning.Text = "WARNING!";
            // 
            // panelTopTitle
            // 
            this.panelTopTitle.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.panelTopTitle.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(45)))), ((int)(((byte)(46)))), ((int)(((byte)(129)))));
            this.panelTopTitle.Controls.Add(this.lblTitle);
            this.panelTopTitle.Location = new System.Drawing.Point(0, 0);
            this.panelTopTitle.Name = "panelTopTitle";
            this.panelTopTitle.Size = new System.Drawing.Size(763, 90);
            this.panelTopTitle.TabIndex = 0;
            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Malgun Gothic", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point);
            this.lblTitle.ForeColor = System.Drawing.SystemColors.Window;
            this.lblTitle.Location = new System.Drawing.Point(205, 25);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(345, 40);
            this.lblTitle.TabIndex = 0;
            this.lblTitle.Text = "Zona de administración";
            // 
            // formAdminLog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(763, 461);
            this.Controls.Add(this.panelChildFormAdmin);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
            this.Name = "formAdminLog";
            this.Text = "Form1";
            this.panelChildFormAdmin.ResumeLayout(false);
            this.panelChildFormAdmin.PerformLayout();
            this.panelTopTitle.ResumeLayout(false);
            this.panelTopTitle.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private Panel panelChildFormAdmin;
        private Panel panelTopTitle;
        private Label lblTitle;
        private Label lblInformacion;
        private Label lblWarning;
        private Label label2;
        private Button btnIngresar;
        private TextBox txtPassword;
    }
}