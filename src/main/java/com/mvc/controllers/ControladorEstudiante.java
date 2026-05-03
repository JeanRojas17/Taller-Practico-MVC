package com.mvc.controllers;

import com.mvc.models.Estudiante;
import com.mvc.services.EstudianteService;
import com.mvc.views.VistaEstudianteSwing;

import java.util.List;

public class ControladorEstudiante {

    private final VistaEstudianteSwing vista;
    private final EstudianteService service;

    public ControladorEstudiante(VistaEstudianteSwing vista, EstudianteService service) {
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
        String apellido = vista.getApellido();
        String correo = vista.getCorreo();

        if(nombre.isEmpty() || apellido.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Apellido son obligatorios.");
            return;
        }

        try {
            Estudiante nuevo = new Estudiante(0, nombre, apellido, correo);

            service.registrarEstudiante(nuevo);
            vista.mostrarMensaje("Estudiante registrado exitosamente.");
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
            vista.mostrarError("Selecciona un estudiante de la tabla para actualizar.");
            return;
        }

        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        String correo = vista.getCorreo();

        if(nombre.isEmpty() || apellido.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Apellido son obligatorios.");
            return;
        }

        try {
            Estudiante actualizado = new Estudiante(id, nombre, apellido, correo);

            service.actualizarEstudiante(actualizado);
            vista.mostrarMensaje("Estudiante actualizado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al actualizar: " +ex.getMessage());
        }
    }

    private void eliminar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona un estudiante de la tabla para eliminar.");
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "¿Estás seguro de que deseas eliminar el estudiante con ID " +id+ "?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if(confirmacion != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            service.eliminarEstudiante(id);
            vista.mostrarMensaje("Estudiante eliminado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch (Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private void refrescar() {
        try {
            List<Estudiante> estudiantes = service.mostrarTodosLosEstudiantes();
            vista.cargarEstudiantes(estudiantes);
        } catch(Exception ex) {
            vista.mostrarError("Error al cargar estudiantes: " +ex.getMessage());
        }
    }
}