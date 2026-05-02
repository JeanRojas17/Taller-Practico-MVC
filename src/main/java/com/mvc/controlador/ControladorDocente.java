package com.mvc.controlador;

import com.mvc.modelo.Docente;
import com.mvc.servicios.DocenteService;
import com.mvc.vista.VistaDocenteSwing;

import java.util.List;

public class ControladorDocente {

    private final VistaDocenteSwing vista;
    private final DocenteService service;

    public ControladorDocente(VistaDocenteSwing vista, DocenteService service) {
        this.vista = vista;
        this.service = service;

        vista.setOnRegistrar(this::registrar);
        vista.setOnActualizar(this::actualizar);
        vista.setOnEliminar(this::eliminar);
        vista.setOnRefrescar(this::refrescar);

        refrescar();
    }

    private void registrar() {
        String nombre = vista.getNombre();
        String especialidad = vista.getEspecialidad();

        if(nombre.isEmpty() || especialidad.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Especialidad son obligatorios.");
            return;
        }

        try {
            Docente nuevo = new Docente(0, nombre, especialidad);

            service.registrarDocente(nuevo);
            vista.mostrarMensaje("Docente registrado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        } catch(Exception ex) {
            vista.mostrarError("Error al registrar: " +ex.getMessage());
        }
    }

    private void actualizar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona un docente de la tabla para actualizar.");
            return;
        }

        String nombre = vista.getNombre();
        String especialidad = vista.getEspecialidad();

        if(nombre.isEmpty() || especialidad.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Especialidad son obligatorios.");
            return;
        }

        try {
            Docente actualizado = new Docente(id, nombre, especialidad);

            service.actualizarDocente(actualizado);
            vista.mostrarMensaje("Docente actualizado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al actualizar: " +ex.getMessage());
        }
    }

    private void eliminar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona un docente de la tabla para eliminar.");
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "Estas seguro de que deseas eliminar el docente con ID " +id+ "?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if(confirmacion != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            service.eliminarDocente(id);
            vista.mostrarMensaje("Docente eliminado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private void refrescar() {
        try {
            List<Docente> docentes = service.mostrarTodosLosDocentes();
            vista.cargarDocentes(docentes);
        } catch(Exception ex) {
            vista.mostrarError("Error al cargar docentes: " +ex.getMessage());
        }
    }
}