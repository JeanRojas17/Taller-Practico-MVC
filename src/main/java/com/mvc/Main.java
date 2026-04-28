package com.mvc;

import com.mvc.controlador.*;
import com.mvc.dao.*;
import com.mvc.servicios.*;
import com.mvc.vista.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("------------------------------");
        System.out.println("Práctica MVC - Sistema Académico UNIAJC");
        System.out.println("------------------------------");

        // ── ESTUDIANTE ──────────────────────────────────
        VistaEstudiante vistaEstudiante = new VistaEstudiante();
        EstudianteDao estudianteDao = new EstudianteDao();
        EstudianteService estudianteService = new EstudianteService(estudianteDao);
        ControladorEstudiante controladorEstudiante = new ControladorEstudiante(vistaEstudiante, estudianteService);

        // Flujo de registro de un estudiante
        controladorEstudiante.mostrarTodosLosEstudiantes();
        controladorEstudiante.registrarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();

        // Flujo de consulta de un estudiante por ID
        int idEstudiante = vistaEstudiante.solicitarIdEstudiante();
        controladorEstudiante.mostrarDetallesEstudiante(idEstudiante);

        // Flujo de actualización de un estudiante por ID
        controladorEstudiante.actualizarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();

        // Flujo de eliminación de un estudiante por ID
        controladorEstudiante.eliminarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();

        // ── DOCENTE ─────────────────────────────────────
        VistaDocente vistaDocente = new VistaDocente();
        DocenteDao docenteDao = new DocenteDao();
        DocenteService docenteService = new DocenteService(docenteDao);
        ControladorDocente controladorDocente = new ControladorDocente(vistaDocente, docenteService);

        // Flujo de registro de un docente
        controladorDocente.mostrarTodosLosDocentes();
        controladorDocente.registrarDocente();
        controladorDocente.mostrarTodosLosDocentes();

        // Flujo de consulta de un docente por ID
        int idDocente = vistaDocente.solicitarIdDocente();
        controladorDocente.mostrarDetallesDocente(idDocente);

        // Flujo de actualización de un docente por ID
        controladorDocente.actualizarDocente();
        controladorDocente.mostrarTodosLosDocentes();

        // Flujo de eliminación de un docente por ID
        controladorDocente.eliminarDocente();
        controladorDocente.mostrarTodosLosDocentes();

        // ── MATERIA ─────────────────────────────────────
        VistaMateria vistaMateria = new VistaMateria();
        MateriaDao materiaDao = new MateriaDao();
        MateriaService materiaService = new MateriaService(materiaDao);
        ControladorMateria controladorMateria = new ControladorMateria(vistaMateria, materiaService);

        // Flujo de registro de una materia
        controladorMateria.mostrarTodasLasMaterias();
        controladorMateria.registrarMateria();
        controladorMateria.mostrarTodasLasMaterias();

        // Flujo de consulta de una materia por ID
        int idMateria = vistaMateria.solicitarIdMateria();
        controladorMateria.mostrarDetallesMateria(idMateria);

        // Flujo de actualización de una materia por ID
        controladorMateria.actualizarMateria();
        controladorMateria.mostrarTodasLasMaterias();

        // Flujo de eliminación de una materia por ID
        controladorMateria.eliminarMateria();
        controladorMateria.mostrarTodasLasMaterias();

        // ── GRUPO ───────────────────────────────────────
        VistaGrupo vistaGrupo = new VistaGrupo();
        GrupoDao grupoDao = new GrupoDao();
        GrupoService grupoService = new GrupoService(grupoDao);
        ControladorGrupo controladorGrupo = new ControladorGrupo(vistaGrupo, grupoService, materiaService, docenteService);

        // Flujo de registro de un grupo
        controladorGrupo.mostrarTodosLosGrupos();
        controladorGrupo.registrarGrupo();
        controladorGrupo.mostrarTodosLosGrupos();

        // Flujo de consulta de un grupo por ID
        int idGrupo = vistaGrupo.solicitarIdGrupo();
        controladorGrupo.mostrarDetallesGrupo(idGrupo);

        // Flujo de actualización de un grupo por ID
        controladorGrupo.actualizarGrupo();
        controladorGrupo.mostrarTodosLosGrupos();

        // Flujo de eliminación de un grupo por ID
        controladorGrupo.eliminarGrupo();
        controladorGrupo.mostrarTodosLosGrupos();

        // ── INSCRIPCION CURSO ────────────────────────────
        VistaInscripcionCurso vistaInscripcion = new VistaInscripcionCurso();
        InscripcionCursoDao inscripcionDao = new InscripcionCursoDao();
        InscripcionCursoService inscripcionService = new InscripcionCursoService(inscripcionDao);
        ControladorInscripcionCurso controladorInscripcion = new ControladorInscripcionCurso(vistaInscripcion, inscripcionService, estudianteService, grupoService);

        // Flujo de registro de una inscripción
        controladorInscripcion.mostrarTodasLasInscripciones();
        controladorInscripcion.registrarInscripcion();
        controladorInscripcion.mostrarTodasLasInscripciones();

        // Flujo de consulta de una inscripción por ID
        int idInscripcion = vistaInscripcion.solicitarIdInscripcion();
        controladorInscripcion.mostrarDetallesInscripcion(idInscripcion);

        // Flujo de actualización de una inscripción por ID
        controladorInscripcion.actualizarInscripcion();
        controladorInscripcion.mostrarTodasLasInscripciones();

        // Flujo de eliminación de una inscripción por ID
        controladorInscripcion.eliminarInscripcion();
        controladorInscripcion.mostrarTodasLasInscripciones();

        // Consultas adicionales de notas por estudiante y grupo
        controladorInscripcion.mostrarNotasPorEstudiante();
        controladorInscripcion.mostrarNotasPorGrupo();
    }
}