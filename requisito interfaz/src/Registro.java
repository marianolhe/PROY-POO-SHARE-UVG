import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registro extends JDialog{
	
	//capos para ingresar lso datos
    private JTextField tfnombre;
    private JTextField tfcorreo;
    private JPasswordField tfcontrasena;
    private JTextField tfapellido;
    private JButton btnregistrarse;
    private JButton btncancelar;
    private JPanel panelregistro;


	//constructor para configuarar elementos gráficos
    public Registro(JFrame parent){
        super(parent);
        setTitle("Crea una nueva cuenta");
        setContentPane(panelregistro);
        setMinimumSize(new Dimension(450,374));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		// Listener para el botón de registro

        btnregistrarse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
		
		// Listener para el botón de cancelar
        btncancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }
	
	// Método para registrar un nuevo usuario

    private void registerUser() {
        String nombre = tfnombre.getText();
        String apellido = tfapellido.getText();
        String correo = tfcorreo.getText();
        String contrasena = tfcontrasena.getText();
		 // Verifica si algún campo está vacío

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
    public Usuarionotused usuario;
	
	 // Método para añadir usuario a la base de datos
    private Usuarionotused addUserToDatabase(String nombre, String apellido, String correo, String contrasena) {
            Usuarionotused usuario = null;

            final String DB_URL = "jdbc:mysql://localhost:3306/proyectopooo";
            final String USERNAME = "root";
            final String PASSWORD = "";

            try{
				 // Conexión a la base de datos-- no funciona 
                Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);


                Statement stmt = conn.createStatement();
                String sql = "INSERT INTO users(nombre, apellido, correo, contrasena)" +
                        "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,nombre);
                preparedStatement.setString(2,apellido);
                preparedStatement.setString(3,correo);
                preparedStatement.setString(4,contrasena);
				
				// Ejecutar la inserción y comprobar si se añadió un nuevo registro


                int addedRows = preparedStatement.executeUpdate();
                if (addedRows > 0) {
					
					// Si se añadió el usuario correctamente, crea una nueva instancia de Usuario
                    usuario = new Usuarionotused();
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
	
	
	// Método principal para iniciar la aplicación
    public static void main(String[] args) {
        Registro registro = new Registro(null);
        Usuarionotused usuario = registro.usuario;
        if (usuario != null) {
            System.out.println("registro exitoso" + usuario.nombre);
        }
        else {
            System.out.println("registro nulo");
        }
    }
}
