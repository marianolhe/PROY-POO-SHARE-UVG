import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InterfazLogin {
    private JFrame frame;
    private GestionLogin gestionLogin;

    public InterfazLogin() {
        gestionLogin = new GestionLogin();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Login");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioLogin();
            }
        });
        panel.add(btnLogin);

        JButton btnRegister = new JButton("Registrarse");
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioRegistro();
            }
        });
        panel.add(btnRegister);

        frame.setVisible(true);
    }

    private void mostrarFormularioLogin() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField correoField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("Correo:"));
        loginPanel.add(correoField);
        loginPanel.add(new JLabel("Contraseña:"));
        loginPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(frame, loginPanel, "Iniciar Sesión", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String correo = correoField.getText();
            String contrasena = new String(passwordField.getPassword());

            try {
                PersonaPlantilla usuario = gestionLogin.iniciarSesion(correo, contrasena);
                if (usuario != null) {
                    JOptionPane.showMessageDialog(frame, "¡Inicio de sesión exitoso!");
                    frame.setVisible(false); // Ocultar la ventana de inicio de sesión
                    gestionLogin.mostrarMenuPorRol(usuario);
                    frame.setVisible(true); // Mostrar nuevamente la ventana de inicio de sesión
                } else {
                    JOptionPane.showMessageDialog(frame, "Correo o contraseña incorrectos.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error al intentar iniciar sesión.");
            }
        }
    }

    private void mostrarFormularioRegistro() {
        JPanel registerPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField correoField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> carreraComboBox = new JComboBox<>(new String[]{
            "Ingeniería en Ciencias de la Computación y Tecnologías de la Información"
        });
        JComboBox<String> rolComboBox = new JComboBox<>(new String[]{"Usuario", "Revisor"});

        registerPanel.add(new JLabel("Nombre:"));
        registerPanel.add(nombreField);
        registerPanel.add(new JLabel("Apellido:"));
        registerPanel.add(apellidoField);
        registerPanel.add(new JLabel("Correo:"));
        registerPanel.add(correoField);
        registerPanel.add(new JLabel("Contraseña:"));
        registerPanel.add(passwordField);
        registerPanel.add(new JLabel("Carrera:"));
        registerPanel.add(carreraComboBox);
        registerPanel.add(new JLabel("Rol:"));
        registerPanel.add(rolComboBox);

        int result = JOptionPane.showConfirmDialog(frame, registerPanel, "Registrarse", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String correo = correoField.getText();
            String contrasena = new String(passwordField.getPassword());
            String carrera = (String) carreraComboBox.getSelectedItem();
            String rol = (String) rolComboBox.getSelectedItem();

            if (validarCorreo(correo) && validarContrasena(contrasena)) {
                try {
                    gestionLogin.registrarUsuario(nombre, apellido, correo, contrasena, carrera, rol);
                    JOptionPane.showMessageDialog(frame, "¡Registro exitoso!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error al intentar registrar el usuario.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Correo o contraseña no cumplen con los requisitos.");
            }
        }
    }

    private boolean validarCorreo(String correo) {
        return correo.contains("@") && correo.endsWith("@uvg.edu.gt");
    }

    private boolean validarContrasena(String contrasena) {
        return contrasena.length() >= 4 && contrasena.length() <= 8 &&
               contrasena.matches(".*[A-Z].*") &&
               contrasena.matches(".*\\d.*") &&
               contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InterfazLogin window = new InterfazLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
