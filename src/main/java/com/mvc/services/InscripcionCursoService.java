package com.mvc.services;

import java.util.List;

import com.mvc.dao.InscripcionCursoDao;
import com.mvc.models.InscripcionCurso;

public class InscripcionCursoService {

    private final InscripcionCursoDao inscripcionCursoDao;

    public InscripcionCursoService(InscripcionCursoDao inscripcionCursoDao) {
        this.inscripcionCursoDao = inscripcionCursoDao;
    }

    public void registrarInscripcion(InscripcionCurso inscripcion) {
        if(inscripcion == null) {
            throw new IllegalArgumentException("La inscripción no puede ser nula.");
        }
        if(inscripcion.getEstudiante() == null || inscripcion.getGrupo() == null) {
            throw new IllegalArgumentException("La inscripción debe tener un estudiante y un grupo asignados.");
        }
        if(inscripcion.getEstado() == null) {
            throw new IllegalArgumentException("El campo estado de la inscripción es obligatorio.");
        }
        
        inscripcionCursoDao.guardarInscripcion(inscripcion);
    }

    public List<InscripcionCurso> mostrarTodasLasInscripciones() {
        return inscripcionCursoDao.obtenerTodasLasInscripciones();
    }

    public InscripcionCurso obtenerInscripcionPorId(int id) {
        return inscripcionCursoDao.obtenerInscripcionPorId(id);
    }

    public void actualizarInscripcion(InscripcionCurso inscripcion) {
        inscripcionCursoDao.actualizarInscripcion(inscripcion);
    }

    public void eliminarInscripcion(int id) {
        inscripcionCursoDao.eliminarInscripcion(id);
    }

    public List<InscripcionCurso> obtenerInscripcionesPorEstudiante(int idEstudiante) {
        return inscripcionCursoDao.obtenerInscripcionesPorEstudiante(idEstudiante);
    }
    
    public List<InscripcionCurso> obtenerInscripcionesPorGrupo(int idGrupo) {
        return inscripcionCursoDao.obtenerInscripcionesPorGrupo(idGrupo);
    }

    public void eliminarEstudianteDeGrupo(int idEstudiante, int idGrupo) {
        inscripcionCursoDao.eliminarInscripcionPorEstudianteYGrupo(idEstudiante, idGrupo);
    }
}