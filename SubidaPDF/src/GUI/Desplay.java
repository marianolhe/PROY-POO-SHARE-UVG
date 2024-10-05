package GUI;

import javax.swing.*;

public class Desplay extends JFrame {
    private JButton botonSeleccionar;

    public Desplay() {
        // Configuración de la ventana
        setTitle("Seleccionar Archivos PDF");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar y configurar el botón
        botonSeleccionar = new JButton("Seleccionar PDF");


        // Añadir el botón a la ventana
        add(botonSeleccionar);
    }
}
