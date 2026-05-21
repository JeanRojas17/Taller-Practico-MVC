package com.mvc.views;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.*;

class VistaPrincipalSwingTest {

    @Test
    @DisplayName("Los menus principales navegan a sus paneles")
    void menusPrincipales_muestranPanelCorrespondiente() throws Exception {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "La prueba Swing requiere entorno grafico.");

        SwingUtilities.invokeAndWait(() -> {
            JPanel inicio = new JPanel();
            JPanel estudiantes = new JPanel();
            JPanel docentes = new JPanel();
            JPanel materias = new JPanel();
            JPanel grupos = new JPanel();
            JPanel inscripciones = new JPanel();
            VistaPrincipalSwing vista = new VistaPrincipalSwing(
                    inicio, estudiantes, docentes, materias, grupos, inscripciones
            );

            try {
                assertTrue(inicio.isVisible());

                clickMenuItem(vista, "Estudiante", "Gestión de Estudiantes");
                assertTrue(estudiantes.isVisible());
                assertFalse(inicio.isVisible());

                clickMenuItem(vista, "Docente", "Gestión de Docentes");
                assertTrue(docentes.isVisible());

                clickMenuItem(vista, "Materia", "Gestión de Materias");
                assertTrue(materias.isVisible());

                clickMenuItem(vista, "Grupo", "Gestión de Grupos");
                assertTrue(grupos.isVisible());

                clickMenuItem(vista, "Inscripción", "Gestión de Inscripciones");
                assertTrue(inscripciones.isVisible());

                clickMenuItem(vista, "Archivo", "Inicio");
                assertTrue(inicio.isVisible());
            } finally {
                vista.dispose();
            }
        });
    }

    private static void clickMenuItem(VistaPrincipalSwing vista, String menuText, String itemText) {
        JMenuItem item = findMenuItem(vista.getJMenuBar(), menuText, itemText);
        assertNotNull(item, "No se encontro el item de menu " + menuText + " > " + itemText);
        item.doClick();
    }

    private static JMenuItem findMenuItem(JMenuBar menuBar, String menuText, String itemText) {
        for(int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if(menu == null || !menuText.equals(menu.getText())) {
                continue;
            }

            for(int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if(item != null && itemText.equals(item.getText())) {
                    return item;
                }
            }
        }

        return null;
    }
}