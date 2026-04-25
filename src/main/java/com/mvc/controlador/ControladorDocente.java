package com.mvc.controlador;

import java.util.ArrayList;
import java.util.List;
import com.mvc.modelo.Docente;
import com.mvc.vista.VistaDocente;

public class ControladorDocente {

    private Docente docente;
    private List<Docente> docentes;
    private VistaDocente vista;

    public ControladorDocente(Docente docente, VistaDocente vista) {
        this.docente = docente;
        this.vista = vista;
        this.docentes = new ArrayList<Docente>();
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public VistaDocente getVista() {
        return vista;
    }

    public void setVista(VistaDocente vista) {
        this.vista = vista;
    }

    public void agregarDocente(Docente docente) {
        docentes.add(docente);
        System.out.println("Docente agregado: " + docente.getNombre());
    }

    public void eliminarDocente(Docente docente) {
        if (docentes.removeIf(doc -> doc.getNombre().equalsIgnoreCase(docente.getNombre()))) {
            System.out.println("Docente eliminado: " + docente.getNombre());
        } else {
            System.out.println("Docente no encontrado para eliminar: " + docente.getNombre());
        }
    }

    public void actualizarDocente(Docente docente) {
        for (int i = 0; i < docentes.size(); i++) {
            if (docentes.get(i).getNombre().equalsIgnoreCase(docente.getNombre())) {
                docentes.set(i, docente);
                System.out.println("Docente modificado: " + docente.getNombre());
                return;
            }
        }
        System.out.println("Docente no encontrado para modificar: " + docente.getNombre());
    }

    public void buscarDocente(String nombre) {
        for (Docente doc : docentes) {
            if (doc.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Docente encontrado: " + doc.getNombre());
                return;
            }
        }
        System.out.println("Docente no encontrado: " + nombre);
    }

    public void actualizarVista() {
        vista.mostrarDetallesDocente(docente);
    }

    public void mostrarTodosLosDocentes() {
        vista.mostrarTodosLosDocentes(docentes);
    }
}