/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ingresarvoto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VotoInterface implements Runnable {
    private final Connection connection;
    private JFrame frame;
    private JComboBox<String> comboBoxDocumento;
    private JTextField textFieldNumeroIdentificacion;

    public VotoInterface(Connection connection) {
        this.connection = connection;
    }

    public void run() {
        // Crear y configurar el marco principal
        frame = new JFrame("Interfaz de Votación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Crear un panel y configurar el diseño
        JPanel panel = new JPanel(new GridLayout(3, 2));

        // Componentes
        JLabel labelDocumento = new JLabel("Tipo de Documento:");
        comboBoxDocumento = new JComboBox<>(new String[]{"Cedula de Ciudadania", "Tarjeta de Identidad", "Cedula de Extranjeria", "Pasaporte"});

        JLabel labelNumeroIdentificacion = new JLabel("Número de Identificación:");
        textFieldNumeroIdentificacion = new JTextField();

        JButton buttonVerificar = new JButton("Verificar");

        // Agregar componentes al panel
        panel.add(labelDocumento);
        panel.add(comboBoxDocumento);
        panel.add(labelNumeroIdentificacion);
        panel.add(textFieldNumeroIdentificacion);
        panel.add(buttonVerificar);

        // Acción del botón
        buttonVerificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int opcionDocumento = comboBoxDocumento.getSelectedIndex() + 1;
                String numeroIdentificacion = textFieldNumeroIdentificacion.getText();

                boolean esValido = verificarDocumento(opcionDocumento, numeroIdentificacion);

                if (esValido) {
                    JOptionPane.showMessageDialog(frame, "Documento válido. Puede proceder a votar.");
                    // Agregar aquí la lógica para permitir al administrador votar
                } else {
                    JOptionPane.showMessageDialog(frame, "Documento no válido. No puede votar.");
                }
            }
        });

        // Agregar el panel al marco
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        // Hacer visible el marco
        frame.setVisible(true);
    }

    // Añadir este método para iniciar la interfaz
    public void iniciarInterfaz() {
        javax.swing.SwingUtilities.invokeLater(this::run);
    }

    private boolean verificarDocumento(int tipoDocumento, String numeroIdentificacion) {
        try {
            String sql = "SELECT * FROM administradores WHERE tipoIdentificacion = ? AND numeroIdentificacion = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Asegúrate de que el índice de tipoDocumento sea correcto y que coincida con los valores de la base de datos
                statement.setString(1, obtenerNombreTipoDocumento(tipoDocumento));
                statement.setString(2, numeroIdentificacion);

                // Imprimir la consulta SQL
                System.out.println("Consulta SQL: " + statement.toString());

                // Ejecutar la consulta
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Usuario encontrado en la base de datos");
                        return true;
                    } else {
                        System.out.println("Usuario NO encontrado en la base de datos");
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Este método podría ser útil para convertir el índice del tipo de documento a su nombre correspondiente
    private String obtenerNombreTipoDocumento(int tipoDocumento) {
        switch (tipoDocumento) {
            case 1:
                return "Cedula de Ciudadania";
            case 2:
                return "Tarjeta de Identidad";
            case 3:
                return "Cedula de Extranjeria";
            case 4:
                return "Pasaporte";
            default:
                throw new IllegalArgumentException("Índice de tipo de documento no válido");
        }
    }
}
