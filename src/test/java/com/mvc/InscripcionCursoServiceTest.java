package com.mvc;

import com.mvc.dao.InscripcionCursoDao;
import com.mvc.models.*;
import com.mvc.services.InscripcionCursoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InscripcionCursoServiceTest {

    private FakeInscripcionCursoDao inscripcionCursoDao;
    private InscripcionCursoService inscripcionCursoService;
    private Estudiante estudiante;
    private Grupo grupo;

    @BeforeEach
    void setUp() {
        inscripcionCursoDao = new FakeInscripcionCursoDao();
        estudiante = new Estudiante(1, "Jean", "Rojas", "jean@email.com");
        Materia materia = new Materia(1, "Bases de Datos", 3);
        Docente docente = new Docente(1, "Ana Torres", "Software");
        grupo = new Grupo(1, materia, docente, "Aula 101", "Lunes 8am");

        inscripcionCursoDao.inscripciones.add(new InscripcionCurso(1, estudiante, grupo, 4.2f, "Aprobado"));
        inscripcionCursoService = new InscripcionCursoService(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Registra inscripciones validas delegando en el DAO")
    void registrarInscripcion_valida_delegaEnDao() {
        InscripcionCurso inscripcion = new InscripcionCurso(2, estudiante, grupo, null, "Inscrito");

        inscripcionCursoService.registrarInscripcion(inscripcion);

        assertSame(inscripcion, inscripcionCursoDao.inscripcionGuardada);
        assertEquals(1, inscripcionCursoDao.guardadas);
    }

    @Test
    @DisplayName("Rechaza inscripciones incompletas y no toca el DAO")
    void registrarInscripcion_invalida_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, null, grupo, null, "Inscrito"))),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, estudiante, null, null, "Inscrito"))),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, estudiante, grupo, null, null)))
        );

        assertEquals(0, inscripcionCursoDao.guardadas);
        assertNull(inscripcionCursoDao.inscripcionGuardada);
    }

    @Test
    @DisplayName("Consulta todas las inscripciones desde el DAO")
    void mostrarTodasLasInscripciones_retornaDatosDelDao() {
        List<InscripcionCurso> inscripciones = inscripcionCursoService.mostrarTodasLasInscripciones();

        assertEquals(1, inscripciones.size());
        assertEquals("Aprobado", inscripciones.get(0).getEstado());
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        InscripcionCurso actualizada = new InscripcionCurso(1, estudiante, grupo, 4.5f, "Aprobado");

        InscripcionCurso encontrada = inscripcionCursoService.obtenerInscripcionPorId(1);
        inscripcionCursoService.actualizarInscripcion(actualizada);
        inscripcionCursoService.eliminarInscripcion(1);

        assertEquals(4.2f, encontrada.getNotaFinal());
        assertSame(actualizada, inscripcionCursoDao.inscripcionActualizada);
        assertEquals(1, inscripcionCursoDao.idEliminado);
    }

    @Test
    @DisplayName("Filtra inscripciones por estudiante y por grupo delegando en el DAO")
    void filtrosPorEstudianteYGrupo_deleganEnDao() {
        List<InscripcionCurso> porEstudiante = inscripcionCursoService.obtenerInscripcionesPorEstudiante(1);
        List<InscripcionCurso> porGrupo = inscripcionCursoService.obtenerInscripcionesPorGrupo(1);

        assertEquals(1, porEstudiante.size());
        assertEquals(1, porGrupo.size());
        assertEquals(1, inscripcionCursoDao.idEstudianteConsultado);
        assertEquals(1, inscripcionCursoDao.idGrupoConsultado);
    }

    @Test
    @DisplayName("Elimina un estudiante de un grupo delegando en el DAO")
    void eliminarEstudianteDeGrupo_delegaEnDao() {
        inscripcionCursoService.eliminarEstudianteDeGrupo(1, 1);

        assertEquals(1, inscripcionCursoDao.idEstudianteEliminado);
        assertEquals(1, inscripcionCursoDao.idGrupoEliminado);
    }

    private static class FakeInscripcionCursoDao extends InscripcionCursoDao {
        private final List<InscripcionCurso> inscripciones = new ArrayList<>();
        private InscripcionCurso inscripcionGuardada;
        private InscripcionCurso inscripcionActualizada;
        private Integer idEliminado;
        private Integer idEstudianteConsultado;
        private Integer idGrupoConsultado;
        private Integer idEstudianteEliminado;
        private Integer idGrupoEliminado;
        private int guardadas;

        @Override
        public void guardarInscripcion(InscripcionCurso inscripcion) {
            inscripcionGuardada = inscripcion;
            guardadas++;
            inscripciones.add(inscripcion);
        }

        @Override
        public List<InscripcionCurso> obtenerTodasLasInscripciones() {
            return List.copyOf(inscripciones);
        }

        @Override
        public InscripcionCurso obtenerInscripcionPorId(int id) {
            return inscripciones.stream()
                    .filter(inscripcion -> inscripcion.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void actualizarInscripcion(InscripcionCurso inscripcion) {
            inscripcionActualizada = inscripcion;
        }

        @Override
        public void eliminarInscripcion(int id) {
            idEliminado = id;
        }

        @Override
        public List<InscripcionCurso> obtenerInscripcionesPorEstudiante(int idEstudiante) {
            idEstudianteConsultado = idEstudiante;
            return inscripciones.stream()
                    .filter(inscripcion -> inscripcion.getEstudiante().getId() == idEstudiante)
                    .toList();
        }

        @Override
        public List<InscripcionCurso> obtenerInscripcionesPorGrupo(int idGrupo) {
            idGrupoConsultado = idGrupo;
            return inscripciones.stream()
                    .filter(inscripcion -> inscripcion.getGrupo().getId() == idGrupo)
                    .toList();
        }

        @Override
        public void eliminarInscripcionPorEstudianteYGrupo(int idEstudiante, int idGrupo) {
            idEstudianteEliminado = idEstudiante;
            idGrupoEliminado = idGrupo;
        }
    }
}