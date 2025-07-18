using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Data.SqlClient;

namespace binaes
{
    internal class AbandonoADO
    {
        public static void Insertar(Abandono Aban)
        {
            string cadena = "Server=LocalHost;Database=BINAES;Trusted_Connection=True";
            using (SqlConnection connection = new SqlConnection(cadena))
            {
                string nonquery = "INSERT INTO ABANDONA (id_area, codigo_usuario, fecha, hora) VALUES (@nuevoid_area, @nuevocodigo_usuario, @nuevafecha, @nuevahora)";

                SqlCommand command = new SqlCommand(nonquery, connection);
                command.Parameters.AddWithValue("@nuevoid_area", Aban.areaID);
                command.Parameters.AddWithValue("@nuevocodigo_usuario", Aban.codigoUsuario);
                command.Parameters.AddWithValue("@nuevafecha", Aban.fechaSalida);
                command.Parameters.AddWithValue("@nuevahora", Aban.horaSalida);

                connection.Open();
                command.ExecuteNonQuery();
                connection.Close();
            }
        }
    }
}
