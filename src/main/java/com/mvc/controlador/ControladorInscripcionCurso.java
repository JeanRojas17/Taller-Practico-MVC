package com.mvc.controlador;

import java.util.List;

import com.mvc.modelo.Estudiante;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.InscripcionCurso;
import com.mvc.servicios.EstudianteService;
import com.mvc.servicios.GrupoService;
import com.mvc.servicios.InscripcionCursoService;
import com.mvc.vista.VistaInscripcionCurso;

public class ControladorInscripcionCurso {

    private static final float NOTA_MINIMA_APROBATORIA = 3.0f;

    private VistaInscripcionCurso vistaInscripcion;
    private InscripcionCursoService inscripcionService;
    private EstudianteService estudianteService;
    private GrupoService grupoService;

    public ControladorInscripcionCurso(VistaInscripcionCurso vistaInscripcion, InscripcionCursoService inscripcionService, EstudianteService estudianteService, GrupoService grupoService) {
        this.vistaInscripcion   = vistaInscripcion;
        this.inscripcionService = inscripcionService;
        this.estudianteService  = estudianteService;
        this.grupoService       = grupoService;
    }

    private String calcularEstado(String notaStr, String estadoElegido) {
        if(notaStr.isBlank()) {
            return estadoElegido;
        }

        float nota = Float.parseFloat(notaStr);
        return nota >= NOTA_MINIMA_APROBATORIA ? "Aprobado" : "Reprobado";
    }

    public void registrarInscripcion() {
        String[] datos = vistaInscripcion.solicitarDatosInscripcion();

        int idEstudiante = Integer.parseInt(datos[0]);
        int idGrupo = Integer.parseInt(datos[1]);
        String notaStr = datos[2];
        String estadoElegido = datos[3];

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(estudiante == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
            return;
        }
        if(grupo == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        Float notaFinal = notaStr.isBlank() ? null : Float.parseFloat(notaStr);
        String estado = calcularEstado(notaStr, estadoElegido);

        InscripcionCurso nuevaInscripcion = new InscripcionCurso(0, estudiante, grupo, notaFinal, estado);
        inscripcionService.registrarInscripcion(nuevaInscripcion);
        vistaInscripcion.mostrarMensaje("Inscripción registrada exitosamente. Estado: " +estado);
    }

    public void mostrarTodasLasInscripciones() {
        vistaInscripcion.mostrarTodasLasInscripciones(inscripcionService.mostrarTodasLasInscripciones());
    }

    public void mostrarDetallesInscripcion(int id) {
        InscripcionCurso inscripcion = inscripcionService.obtenerInscripcionPorId(id);

        if(inscripcion != null) {
            vistaInscripcion.mostrarDetallesInscripcion(inscripcion);
        } else {
            vistaInscripcion.mostrarMensaje("No se encontró una inscripción con el ID proporcionado.");
        }
    }

    public void actualizarInscripcion() {
        int id = vistaInscripcion.solicitarIdInscripcion();
        InscripcionCurso inscripcionExistente = inscripcionService.obtenerInscripcionPorId(id);

        if(inscripcionExistente == null) {
            vistaInscripcion.mostrarMensaje("No se encontró una inscripción con el ID proporcionado.");
            return;
        }

        String[] datos = vistaInscripcion.solicitarDatosInscripcionActualizados();
        int idEstudiante = Integer.parseInt(datos[0]);
        int idGrupo = Integer.parseInt(datos[1]);
        String notaStr = datos[2];
        String estadoElegido = datos[3];

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(estudiante == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
            return;
        }
        if(grupo == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        Float notaFinal = notaStr.isBlank() ? null : Float.parseFloat(notaStr);
        String estado = calcularEstado(notaStr, estadoElegido);

        InscripcionCurso inscripcionActualizada = new InscripcionCurso(inscripcionExistente.getId(), estudiante, grupo, notaFinal, estado);
        inscripcionService.actualizarInscripcion(inscripcionActualizada);
        vistaInscripcion.mostrarMensaje("Inscripción actualizada exitosamente. Estado: " +estado);
    }

    public void eliminarInscripcion() {
        int id = vistaInscripcion.solicitarIdInscripcionParaEliminar();
        inscripcionService.eliminarInscripcion(id);
        vistaInscripcion.mostrarMensaje("Inscripción eliminada exitosamente.");
    }

    public void mostrarNotasPorEstudiante() {
        int idEstudiante = vistaInscripcion.solicitarIdEstudianteParaConsulta();
        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);

        if(estudiante == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
            return;
        }

        List<InscripcionCurso> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(idEstudiante);
        vistaInscripcion.mostrarNotasPorEstudiante(inscripciones);
    }

    public void mostrarNotasPorGrupo() {
        int idGrupo = vistaInscripcion.solicitarIdGrupoParaConsulta();
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(grupo == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        List<InscripcionCurso> inscripciones = inscripcionService.obtenerInscripcionesPorGrupo(idGrupo);
        vistaInscripcion.mostrarNotasPorGrupo(inscripciones);
    }

    public void eliminarEstudianteDeGrupo() {
        int[] datos = vistaInscripcion.solicitarDatosParaEliminarEstudianteDeGrupo();

        int idEstudiante = datos[0];
        int idGrupo = datos[1];

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(idEstudiante);
        Grupo grupo = grupoService.obtenerGrupoPorId(idGrupo);

        if(estudiante == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un estudiante con el ID proporcionado.");
            return;
        }
        if(grupo == null) {
            vistaInscripcion.mostrarMensaje("No se encontró un grupo con el ID proporcionado.");
            return;
        }

        inscripcionService.eliminarEstudianteDeGrupo(idEstudiante, idGrupo);
        vistaInscripcion.mostrarMensaje("Estudiante " +estudiante.getNombre()+ " " +estudiante.getApellido()+ " eliminado del grupo " +grupo.getId()+ " - " +grupo.getMateria().getNombreMateria()+ " exitosamente.");
    }
}