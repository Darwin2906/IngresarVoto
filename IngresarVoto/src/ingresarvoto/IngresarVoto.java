/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ingresarvoto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.SwingUtilities;

public class IngresarVoto {
    public static void main(String[] args) {
        // Configuración de la conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/votasoft";
        String user = "root";
        String password = "123456789";

        try {
            // Conexión a la base de datos
            Connection connection = DriverManager.getConnection(url, user, password);

            // Crear la interfaz de voto y pasar la conexión a la base de datos
            VotoInterface votoInterface = new VotoInterface(connection);

            // Iniciar la interfaz de voto
            SwingUtilities.invokeLater(votoInterface::iniciarInterfaz);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
