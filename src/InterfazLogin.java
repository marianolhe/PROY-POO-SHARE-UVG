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
            Image icon = ImageIO.read(new File("resources\\icon_sin_fondo.png"));
            frame.setIconImage(icon);  
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 350);
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
    
        // Crear un panel con fondo
        JPanel panelConFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    // Cargar la imagen de fondo
                    Image backgroundImage = ImageIO.read(new File("resources\\fondo_login.png")); // Ruta de tu imagen
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Dibujar la imagen
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    
        panelConFondo.setLayout(new BoxLayout(panelConFondo, BoxLayout.Y_AXIS)); // Usar BoxLayout
        frame.getContentPane().add(panelConFondo, BorderLayout.CENTER); // Añadir el panel con fondo al JFrame
    
        // Margen superior 
        panelConFondo.add(Box.createVerticalStrut(145));
    
        Dimension btnSize = new Dimension(128, 30); // Definir tamaño común para los botones

        // Botón de Iniciar sesión con fondo
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(btnSize)); // Tamaño del botón
        btnLogin.setMaximumSize(btnSize);
        btnLogin.setMinimumSize(btnSize);
        btnLogin.setBackground(new Color(114, 168, 63)); // Color de fondo 
        btnLogin.setBorderPainted(false); // Sin borde
        btnLogin.setForeground(Color.WHITE); // Color del texto
        btnLogin.setFont(new Font("Helvetica", Font.BOLD, 14)); // Fuente en negrita
        btnLogin.setFocusPainted(false); // Quitar borde de enfoque
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano al pasar por encima
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioLogin();
            }
        });
        panelConFondo.add(btnLogin);
    
        // Espacio entre los botones
        panelConFondo.add(Box.createVerticalStrut(5));
    
        // Botón de Registrarse con fondo
        JButton btnRegister = new JButton("Registrarse");
        btnRegister.setPreferredSize(new Dimension(btnSize)); // Tamaño del botón
        btnRegister.setMaximumSize(btnSize);
        btnRegister.setMinimumSize(btnSize);
        btnRegister.setBackground(new Color(114, 168, 63)); // Color de fondo 
        btnRegister.setBorderPainted(false); // Sin borde
        btnRegister.setForeground(Color.WHITE); // Color del texto
        btnRegister.setFont(new Font("Helvetica", Font.BOLD, 14)); // Fuente en negrita
        btnRegister.setFocusPainted(false); // Quitar borde de enfoque
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano al pasar por encima
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioRegistro();
            }
        });
        panelConFondo.add(btnRegister);
    
        // Espacio entre los botones
        panelConFondo.add(Box.createVerticalStrut(8));
    
        // Botón de Salir sin fondo, pero con texto verde
        JButton btnExit = new JButton("Salir");
        btnExit.setPreferredSize(new Dimension(200, 40)); // Botón más pequeño
        btnExit.setOpaque(false); // Hacer que el fondo sea transparente
        btnExit.setContentAreaFilled(false); // El área de contenido sin relleno
        btnExit.setBorderPainted(false); // Sin borde
        btnExit.setForeground(new Color(114, 168, 63)); // Color del texto verde
        btnExit.setFont(new Font("Helvetica", Font.BOLD, 14)); // Fuente en negrita
        btnExit.setFocusPainted(false); // Quitar borde de enfoque
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia el cursor a mano al pasar por encima
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });
        panelConFondo.add(btnExit);
    
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
