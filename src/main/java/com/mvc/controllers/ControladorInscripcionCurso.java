package com.mvc.controllers;

import java.math.BigDecimal;
import java.util.List;

import com.mvc.config.ConfiguracionApp;
import com.mvc.models.Estudiante;
import com.mvc.models.Grupo;
import com.mvc.models.InscripcionCurso;
import com.mvc.services.AuditoriaService;
import com.mvc.services.EstudianteService;
import com.mvc.services.GrupoService;
import com.mvc.services.InscripcionCursoService;
import com.mvc.views.VistaInscripcionCursoSwing;

public class ControladorInscripcionCurso {

    private static final BigDecimal NOTA_MINIMA_APROBATORIA = new BigDecimal("3.0");

    private final VistaInscripcionCursoSwing vista;
    private final InscripcionCursoService inscripcionService;
    private final EstudianteService estudianteService;
    private final GrupoService grupoService;
    private final Runnable notificar;

    public ControladorInscripcionCurso(VistaInscripcionCursoSwing vista, InscripcionCursoService inscripcionService, EstudianteService estudianteService, GrupoService grupoService) {
        this(vista, inscripcionService, estudianteService, grupoService, null);
    }

    public ControladorInscripcionCurso(VistaInscripcionCursoSwing vista, InscripcionCursoService inscripcionService, EstudianteService estudianteService, GrupoService grupoService, Runnable notificar) {
        this.vista = vista;
        this.inscripcionService = inscripcionService;
        this.estudianteService = estudianteService;
        this.grupoService = grupoService;
        this.notificar = notificar;

        vista.setOnRegistrar(this::registrar);
        vista.setOnActualizar(this::actualizar);
        vista.setOnEliminar(this::eliminar);
        vista.setOnRefrescar(this::refrescar);
        vista.setOnNotasEstudiante(this::mostrarNotasPorEstudiante);
        vista.setOnNotasGrupo(this::mostrarNotasPorGrupo);

        refrescar();
    }

    private String calcularEstado(BigDecimal nota, String estadoElegido) {
        if(nota == null) {
            return estadoElegido;
        }

        return nota.compareTo(NOTA_MINIMA_APROBATORIA) >= 0 ? "Aprobado" : "Reprobado";
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
            if (estudiante == null) return;

            Grupo grupo = obtenerGrupo(idGrupoTexto);
            if (grupo == null) return;

            BigDecimal notaFinal = notaStr.isBlank() ? null : new BigDecimal(notaStr);
            String estado = calcularEstado(notaFinal, estadoElegido);

            InscripcionCurso nueva = new InscripcionCurso(0, estudiante, grupo, notaFinal, estado);

            inscripcionService.registrarInscripcion(nueva);
            AuditoriaService.getInstance().registrar("CREAR", "InscripcionCurso", "Inscripción registrada: Estudiante ID " +idEstudianteTexto+ ", Grupo ID " +idGrupoTexto+ ", Estado: " +estado);
            vista.mostrarMensaje("Inscripción registrada exitosamente. Estado: " +estado);
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
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
            if (estudiante == null) return;

            Grupo grupo = obtenerGrupo(idGrupoTexto);
            if (grupo == null) return;

            BigDecimal notaFinal = notaStr.isBlank() ? null : new BigDecimal(notaStr);
            String estado = calcularEstado(notaFinal, estadoElegido);

            InscripcionCurso actualizada = new InscripcionCurso(id, estudiante, grupo, notaFinal, estado);

            inscripcionService.actualizarInscripcion(actualizada);
            AuditoriaService.getInstance().registrar("ACTUALIZAR", "InscripcionCurso", "Inscripción ID " +id+ " actualizada. Estado: " +estado);
            vista.mostrarMensaje("Inscripción actualizada exitosamente. Estado: " +estado);
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
        } catch(NumberFormatException ex) {
            vista.mostrarError("Los campos ID Estudiante e ID Grupo deben ser números enteros, y Nota Final debe ser numérica.");
        } catch(IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
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

        if(ConfiguracionApp.getInstance().isConfirmarEliminacion()) {
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                vista,
                "¿Estas seguro de que deseas eliminar la inscripción con ID " +id+ "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion != javax.swing.JOptionPane.YES_OPTION) return;
        }

        try {
            inscripcionService.eliminarInscripcion(id);
            AuditoriaService.getInstance().registrar("ELIMINAR", "InscripcionCurso", "Inscripción ID " +id+ " eliminada.");
            vista.mostrarMensaje("Inscripción eliminada exitosamente.");
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private void mostrarNotasPorEstudiante() {
        Integer idEstudiante = vista.solicitarIdEstudianteParaConsulta();
        if (idEstudiante == null) return;

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
        if (idGrupo == null) return;

        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(grupo == null) {
            vista.mostrarError("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        List<InscripcionCurso> inscripciones = inscripcionService.obtenerInscripcionesPorGrupo(idGrupo);
        vista.cargarInscripciones(inscripciones);
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