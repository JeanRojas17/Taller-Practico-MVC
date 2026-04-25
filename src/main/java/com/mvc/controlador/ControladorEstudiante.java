package com.mvc.controlador;

import java.util.ArrayList;
import java.util.List;

import com.mvc.modelo.Estudiante;
import com.mvc.servicios.EstudianteService;
import com.mvc.vista.VistaEstudiante;

public class ControladorEstudiante {

    private VistaEstudiante vistaEstudiante;
    private EstudianteService estudianteService;

    public ControladorEstudiante(VistaEstudiante vistaEstudiante, EstudianteService estudianteService) {
        this.vistaEstudiante = vistaEstudiante;
        this.estudianteService = estudianteService;
    }

    public void registrarEstudiante() {
        // Lógica para registrar un estudiante

        Estudiante nuevoEstudiante = vistaEstudiante.solicitarDatosEstudiante();

        estudianteService.registrarEstudiante(nuevoEstudiante);
        vistaEstudiante.mostrarMensaje("Estudiante registrado exitosamente.");
    }

    public void mostrarTodosLosEstudiantes() {
        // Lógica para mostrar todos los estudiantes
        vistaEstudiante.mostrarTodosLosEstudiantes(estudianteService.mostrarTodosLosEstudiantes());
    }

    public void mostrarDetallesEstudiante(int id) {
        // Lógica para mostrar los detalles de un estudiante específico
        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(id);

        if(estudiante != null) {
            vistaEstudiante.mostrarDetallesEstudiante(estudiante);
        } else {
            vistaEstudiante.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
        }
    }

    public void actualizarEstudiante() {
        // Lógica para actualizar un estudiante
        int id = vistaEstudiante.solicitarIdEstudiante();
        Estudiante estudianteExistente = estudianteService.obtenerEstudiantePorId(id);

        if(estudianteExistente != null) {
            Estudiante estudianteActualizado = vistaEstudiante.solicitarDatosEstudianteActualizados(estudianteExistente);
            estudianteService.actualizarEstudiante(estudianteActualizado);
            vistaEstudiante.mostrarMensaje("Estudiante actualizado exitosamente.");
        } else {
            vistaEstudiante.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
        }
    }

    public void eliminarEstudiante() {
        // Lógica para eliminar un estudiante
        int id = vistaEstudiante.solicitarIdEstudianteParaEliminar();
        estudianteService.eliminarEstudiante(id);
        vistaEstudiante.mostrarMensaje("Estudiante eliminado exitosamente.");
    }
}