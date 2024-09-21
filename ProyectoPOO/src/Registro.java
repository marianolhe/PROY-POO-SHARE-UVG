import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registro extends JDialog{
    private JTextField tfnombre;
    private JTextField tfcorreo;
    private JPasswordField tfcontrasena;
    private JTextField tfapellido;
    private JButton btnregistrarse;
    private JButton btncancelar;
    private JPanel panelregistro;

    public Registro(JFrame parent){
        super(parent);
        setTitle("Crea una nueva cuenta");
        setContentPane(panelregistro);
        setMinimumSize(new Dimension(450,374));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        btnregistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btncancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String nombre = tfnombre.getText();
        String apellido = tfapellido.getText();
        String correo = tfcorreo.getText();
        String contrasena = tfcontrasena.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa todos los campos",
                    "Intenta de nuevo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

       usuario = addUserToDatabase(nombre,apellido,correo,contrasena);
        if (usuario == null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Fallo en registrar nuevo usuario",
                    "Intenta de nuevo",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    public Usuario usuario;
    private Usuario addUserToDatabase(String nombre, String apellido, String correo, String contrasena) {
            Usuario usuario = null;

            final String DB_URL = "jdbc:mysql://localhost:3306/proyectopooo";
            final String USERNAME = "root";
            final String PASSWORD = "";

            try{
                Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);


                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO users(nombre, apellido, correo, contrasena)" +
                        "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,nombre);
                preparedStatement.setString(2,apellido);
                preparedStatement.setString(3,correo);
                preparedStatement.setString(4,contrasena);


                int addedRows = preparedStatement.executeUpdate();
                if (addedRows > 0) {
                    usuario = new Usuario();
                    usuario.nombre = nombre;
                    usuario.apellido = apellido;
                    usuario.correo = correo;
                    usuario.contrasena = contrasena;



                }

                stmt.close();
                conn.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return usuario;
    }

    public static void main(String[] args) {
        Registro registro = new Registro(null);
        Usuario usuario = registro.usuario;
        if (usuario != null) {
            System.out.println("registro exitoso" + usuario.nombre);
        }
        else {
            System.out.println("registro nulo");
        }
    }
}
