package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SeleccionArchivos implements ActionListener {
	@Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser selectorArchivos = new JFileChooser(); // Crear un objeto JFileChooser 
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selectorArchivos.setMultiSelectionEnabled(false);
        selectorArchivos.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

        int resultado = selectorArchivos.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = selectorArchivos.getSelectedFile();
            try {
                // Carpeta donde se guardaran los archivos (por ahora)
                File carpetaDestino = new File(System.getProperty("user.dir") + File.separator + "archivos_pdf");
                // Validacion para que la carpeta se cree correctamente
                if (!carpetaDestino.exists()) {
                    boolean creada = carpetaDestino.mkdir();
                    if (!creada) {
                        JOptionPane.showMessageDialog(null, "Error al crear la carpeta: " + carpetaDestino.getPath(), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Crear una instancia de Archivo para el archivo PDF seleccionado
                File archivoDestino = new File(carpetaDestino, archivoSeleccionado.getName());

                // Copiar el archivo seleccionado a la carpeta de destino
                Files.copy(archivoSeleccionado.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mostrar mensaje de éxito si el archivo se copiar correctamente a la nueva carpeta
                JOptionPane.showMessageDialog(null, "Archivo guardado correctamente en la carpeta: " + carpetaDestino.getPath());
            } catch (IOException ex) {
                // Mensaje de error si no se puede copiar correctamente el archivo
                JOptionPane.showMessageDialog(null, "Error al copiar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}