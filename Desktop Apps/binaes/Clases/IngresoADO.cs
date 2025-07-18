using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Data.SqlClient;

namespace binaes
{
    internal class IngresoADO
    {
        public static void Insertar(Ingreso ing)
        {
            string cadena = "Server=LocalHost;Database=BINAES;Trusted_Connection=True";
            using (SqlConnection connection = new SqlConnection(cadena))
            {
                string nonquery = "INSERT INTO INGRESA (id_area, codigo_usuario, fecha) VALUES (@nuevoid_area, @nuevocodigo_usuario, @nuevafecha)";

                SqlCommand command = new SqlCommand(nonquery, connection);
                command.Parameters.AddWithValue("@nuevoid_area", ing.areaID);
                command.Parameters.AddWithValue("@nuevocodigo_usuario", ing.codigoUsuario);
                command.Parameters.AddWithValue("@nuevafecha", ing.fechaEntrada);
                command.Parameters.AddWithValue("@nuevahora", ing.horaEntrada);

                connection.Open();
                command.ExecuteNonQuery();
                connection.Close();
            }
        }
    }
}
