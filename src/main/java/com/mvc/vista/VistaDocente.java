package com.mvc.vista;

import java.util.List;
import com.mvc.modelo.Docente;

public class VistaDocente {

    public void mostrarDetallesDocente(Docente docente) {
        System.out.println("Detalles del Docente:");
        System.out.println("Nombre: " +docente.getNombre());
        System.out.println("Edad: " +docente.getEdad());
        System.out.println("");
    }

    public void mostrarTodosLosDocentes(List<Docente> docentes) {
        System.out.println("\nLista de Docentes:");
        for(Docente docente : docentes) {
            mostrarDetallesDocente(docente);
            System.out.println("-------------------");
        }
    }
}