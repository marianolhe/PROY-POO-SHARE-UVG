import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InterfazLogin {
    private JFrame frame;
    private GestionLogin gestionLogin;

    public InterfazLogin() {
        gestionLogin = new GestionLogin();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("LOGIN");

        // Cargar la imagen del icono
        try {
            Image icon = ImageIO.read(new File("resources\\icon sin fondo.png")); 
            frame.setIconImage(icon);  
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Cambié el layout para centrar los componentes verticalmente

        panel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el panel dentro del JFrame
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // Botón de Iniciar sesión
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(200, 40)); // Botón más pequeño
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioLogin();
            }
        });
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        panel.add(btnLogin);

        // Espacio entre los botones
        panel.add(Box.createVerticalStrut(10));

        // Botón de Registrarse
        JButton btnRegister = new JButton("Registrarse");
        btnRegister.setPreferredSize(new Dimension(200, 40)); // Botón más pequeño
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioRegistro();
            }
        });
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        panel.add(btnRegister);

        // Espacio entre los botones
        panel.add(Box.createVerticalStrut(10));

        // Botón de Salir
        JButton btnExit = new JButton("Salir");
        btnExit.setPreferredSize(new Dimension(200, 40)); // Botón más pequeño
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        panel.add(btnExit);

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
                    new InterfazLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
