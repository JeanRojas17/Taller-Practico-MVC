package com.mvc.controlador;

import java.util.List;

import com.mvc.modelo.Estudiante;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.InscripcionCurso;
import com.mvc.servicios.EstudianteService;
import com.mvc.servicios.GrupoService;
import com.mvc.servicios.InscripcionCursoService;
import com.mvc.vista.VistaInscripcionCursoSwing;

public class ControladorInscripcionCurso {

    private static final float NOTA_MINIMA_APROBATORIA = 3.0f;

    private final VistaInscripcionCursoSwing vista;
    private final InscripcionCursoService inscripcionService;
    private final EstudianteService estudianteService;
    private final GrupoService grupoService;

    public ControladorInscripcionCurso(VistaInscripcionCursoSwing vista, InscripcionCursoService inscripcionService, EstudianteService estudianteService, GrupoService grupoService) {
        this.vista = vista;
        this.inscripcionService = inscripcionService;
        this.estudianteService = estudianteService;
        this.grupoService = grupoService;

        vista.setOnRegistrar(this::registrar);
        vista.setOnActualizar(this::actualizar);
        vista.setOnEliminar(this::eliminar);
        vista.setOnRefrescar(this::refrescar);
        vista.setOnNotasEstudiante(this::mostrarNotasPorEstudiante);
        vista.setOnNotasGrupo(this::mostrarNotasPorGrupo);
        vista.setOnEliminarEstudianteGrupo(this::eliminarEstudianteDeGrupo);

        refrescar();
    }

    private String calcularEstado(String notaStr, String estadoElegido) {
        if(notaStr.isBlank()) {
            return estadoElegido;
        }

        float nota = Float.parseFloat(notaStr);
        return nota >= NOTA_MINIMA_APROBATORIA ? "Aprobado" : "Reprobado";
    }

    private void registrar() {
        String idEstudianteTexto = vista.getIdEstudianteTexto();
        String idGrupoTexto = vista.getIdGrupoTexto();
        String notaStr = vista.getNotaFinalTexto();
        String estadoElegido = vista.getEstado();

        if(idEstudianteTexto.isEmpty() || idGrupoTexto.isEmpty() || estadoElegido == null || estadoElegido.isEmpty()) {
            vista.mostrarError("Los campos ID Estudiante, ID Grupo y Estado son obligatorios.");
            return;
        }

        try {
            Estudiante estudiante = obtenerEstudiante(idEstudianteTexto);
            if(estudiante == null) return;

            Grupo grupo = obtenerGrupo(idGrupoTexto);
            if(grupo == null) return;

            Float notaFinal = notaStr.isBlank() ? null : Float.parseFloat(notaStr);
            String estado = calcularEstado(notaStr, estadoElegido);

            InscripcionCurso nueva = new InscripcionCurso(0, estudiante, grupo, notaFinal, estado);

            inscripcionService.registrarInscripcion(nueva);
            vista.mostrarMensaje("Inscripción registrada exitosamente. Estado: " +estado);
            vista.limpiarCampos();
            refrescar();
        } catch(NumberFormatException ex) {
            vista.mostrarError("Los campos ID Estudiante e ID Grupo deben ser números enteros, y Nota Final debe ser numérica.");
        } catch(IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        } catch(Exception ex) {
            vista.mostrarError("Error al registrar: " +ex.getMessage());
        }
    }

    private void actualizar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona una inscripción de la tabla para actualizar.");
            return;
        }

        String idEstudianteTexto = vista.getIdEstudianteTexto();
        String idGrupoTexto = vista.getIdGrupoTexto();
        String notaStr = vista.getNotaFinalTexto();
        String estadoElegido = vista.getEstado();

        if(idEstudianteTexto.isEmpty() || idGrupoTexto.isEmpty() || estadoElegido == null || estadoElegido.isEmpty()) {
            vista.mostrarError("Los campos ID Estudiante, ID Grupo y Estado son obligatorios.");
            return;
        }

        try {
            Estudiante estudiante = obtenerEstudiante(idEstudianteTexto);
            if(estudiante == null) return;

            Grupo grupo = obtenerGrupo(idGrupoTexto);
            if(grupo == null) return;

            Float notaFinal = notaStr.isBlank() ? null : Float.parseFloat(notaStr);
            String estado = calcularEstado(notaStr, estadoElegido);

            InscripcionCurso actualizada = new InscripcionCurso(id, estudiante, grupo, notaFinal, estado);

            inscripcionService.actualizarInscripcion(actualizada);
            vista.mostrarMensaje("Inscripción actualizada exitosamente. Estado: " +estado);
            vista.limpiarCampos();
            refrescar();
        } catch(NumberFormatException ex) {
            vista.mostrarError("Los campos ID Estudiante e ID Grupo deben ser números enteros, y Nota Final debe ser numérica.");
        } catch(Exception ex) {
            vista.mostrarError("Error al actualizar: " +ex.getMessage());
        }
    }

    private void eliminar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona una inscripción de la tabla para eliminar.");
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "Estas seguro de que deseas eliminar la inscripción con ID " +id+ "?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if(confirmacion != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            inscripcionService.eliminarInscripcion(id);
            vista.mostrarMensaje("Inscripción eliminada exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private void mostrarNotasPorEstudiante() {
        Integer idEstudiante = vista.solicitarIdEstudianteParaConsulta();
        if(idEstudiante == null) return;

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);

        if(estudiante == null) {
            vista.mostrarError("No se encontró un estudiante con el ID proporcionado.");
            return;
        }

        List<InscripcionCurso> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(idEstudiante);
        vista.cargarInscripciones(inscripciones);
    }

    private void mostrarNotasPorGrupo() {
        Integer idGrupo = vista.solicitarIdGrupoParaConsulta();
        if(idGrupo == null) return;

        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(grupo == null) {
            vista.mostrarError("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        List<InscripcionCurso> inscripciones = inscripcionService.obtenerInscripcionesPorGrupo(idGrupo);
        vista.cargarInscripciones(inscripciones);
    }

    private void eliminarEstudianteDeGrupo() {
        int[] datos = vista.solicitarDatosParaEliminarEstudianteDeGrupo();
        if(datos == null) return;

        int idEstudiante = datos[0];
        int idGrupo = datos[1];

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(estudiante == null) {
            vista.mostrarError("No se encontró un estudiante con el ID proporcionado.");
            return;
        }
        if(grupo == null) {
            vista.mostrarError("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        try {
            inscripcionService.eliminarEstudianteDeGrupo(idEstudiante, idGrupo);
            vista.mostrarMensaje("Estudiante eliminado del grupo exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar estudiante del grupo: " +ex.getMessage());
        }
    }

    private Estudiante obtenerEstudiante(String idEstudianteTexto) {
        int idEstudiante = Integer.parseInt(idEstudianteTexto);
        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);

        if(estudiante == null) {
            vista.mostrarError("No se encontró un estudiante con el ID proporcionado.");
        }

        return estudiante;
    }

    private Grupo obtenerGrupo(String idGrupoTexto) {
        int idGrupo = Integer.parseInt(idGrupoTexto);
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(grupo == null) {
            vista.mostrarError("No se encontró un grupo con el ID proporcionado.");
        }

        return grupo;
    }

    private void refrescar() {
        try {
            List<InscripcionCurso> inscripciones = inscripcionService.mostrarTodasLasInscripciones();
            vista.cargarInscripciones(inscripciones);
        } catch(Exception ex) {
            vista.mostrarError("Error al cargar inscripciones: " +ex.getMessage());
        }
    }
}