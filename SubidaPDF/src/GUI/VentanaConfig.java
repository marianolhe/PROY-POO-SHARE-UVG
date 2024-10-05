package GUI;
import javax.swing.*;
import java.awt.*;

public class VentanaConfig extends JFrame {
	private static final long serialVersionUID = 1L; // Para evitar problemas de compatibilidad
    private JButton botonSeleccionar;

    public VentanaConfig() {
    	// Configuración de la ventana
        setTitle("Seleccionar Archivos PDF");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Para que el boton quede centrado en la ventana

        // Inicializar y configurar el botón
        botonSeleccionar = new JButton("Seleccionar archivo");
        botonSeleccionar.setPreferredSize(new Dimension(150, 40)); 
        botonSeleccionar.addActionListener(new SeleccionArchivos());
      
        GridBagConstraints gbc = new GridBagConstraints(); // Configuracion para centrar el boton
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Añadir el botón a la ventana
        add(botonSeleccionar, gbc);
    }
}
