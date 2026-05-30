package com.mvc.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.mvc.dao.InscripcionCursoDao;
import com.mvc.models.InscripcionCurso;

public class InscripcionCursoService {

    private static final BigDecimal NOTA_MINIMA = BigDecimal.ZERO;
    private static final BigDecimal NOTA_MAXIMA = new BigDecimal("5.0");

    private static final Set<String> ESTADOS_SIN_NOTA = Set.of("Inscrito", "En curso", "Retirado");

    private static final Set<String> ESTADOS_CON_NOTA = Set.of("Aprobado", "Reprobado");

    private final InscripcionCursoDao inscripcionCursoDao;

    public InscripcionCursoService(InscripcionCursoDao inscripcionCursoDao) {
        this.inscripcionCursoDao = inscripcionCursoDao;
    }

    public void registrarInscripcion(InscripcionCurso inscripcion) {
        validarInscripcion(inscripcion);
        inscripcionCursoDao.guardarInscripcion(inscripcion);
    }

    public List<InscripcionCurso> mostrarTodasLasInscripciones() {
        return inscripcionCursoDao.obtenerTodasLasInscripciones();
    }

    public InscripcionCurso obtenerInscripcionPorId(int id) {
        return inscripcionCursoDao.obtenerInscripcionPorId(id);
    }

    public void actualizarInscripcion(InscripcionCurso inscripcion) {
        validarInscripcion(inscripcion);
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

    private void validarInscripcion(InscripcionCurso inscripcion) {
        if(inscripcion == null) {
            throw new IllegalArgumentException("La inscripción no puede ser nula.");
        }

        if(inscripcion.getEstudiante() == null || inscripcion.getGrupo() == null) {
            throw new IllegalArgumentException("La inscripción debe tener un estudiante y un grupo asignados.");
        }

        if(inscripcion.getEstado() == null || inscripcion.getEstado().isBlank()) {
            throw new IllegalArgumentException("El campo estado de la inscripción es obligatorio.");
        }

        BigDecimal nota = inscripcion.getNotaFinal();

        if(nota != null) {
            if(nota.compareTo(NOTA_MINIMA) < 0 || nota.compareTo(NOTA_MAXIMA) > 0) {
                throw new IllegalArgumentException("La nota final debe estar entre 0.0 y 5.0.");
            }
            if(!ESTADOS_CON_NOTA.contains(inscripcion.getEstado())) {
                throw new IllegalArgumentException(
                    "El estado '" +inscripcion.getEstado()+ "' no es válido para una inscripción con nota. " +
                    "El estado se calcula automáticamente como Aprobado o Reprobado."
                );
            }
        } else {
            if(!ESTADOS_SIN_NOTA.contains(inscripcion.getEstado())) {
                throw new IllegalArgumentException(
                    "El estado '" +inscripcion.getEstado()+ "' no es válido. " +
                    "Los estados permitidos sin nota son: Inscrito, En curso, Retirado."
                );
            }
        }
    }
}