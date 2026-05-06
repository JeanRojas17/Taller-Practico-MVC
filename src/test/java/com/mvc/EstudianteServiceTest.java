package com.mvc;

import com.mvc.dao.EstudianteDao;
import com.mvc.models.Estudiante;
import com.mvc.services.EstudianteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteServiceTest {

    private FakeEstudianteDao estudianteDao;
    private EstudianteService estudianteService;

    @BeforeEach
    void setUp() {
        estudianteDao = new FakeEstudianteDao();
        estudianteDao.estudiantes.add(new Estudiante(1, "Jean", "Rojas", "jean@email.com"));
        estudianteDao.estudiantes.add(new Estudiante(2, "Laura", "Diaz", "laura@email.com"));
        estudianteService = new EstudianteService(estudianteDao);
    }

    @Test
    @DisplayName("Registra estudiantes validos delegando en el DAO")
    void registrarEstudiante_valido_delegaEnDao() {
        Estudiante estudiante = new Estudiante(3, "Sofia", "Lopez", "sofia@email.com");

        estudianteService.registrarEstudiante(estudiante);

        assertSame(estudiante, estudianteDao.estudianteGuardado);
        assertEquals(1, estudianteDao.guardados);
    }

    @Test
    @DisplayName("Rechaza estudiantes sin datos obligatorios y no toca el DAO")
    void registrarEstudiante_invalido_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(new Estudiante(3, null, "Lopez", "sofia@email.com"))),
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(new Estudiante(3, "Sofia", null, "sofia@email.com")))
        );

        assertEquals(0, estudianteDao.guardados);
        assertNull(estudianteDao.estudianteGuardado);
    }

    @Test
    @DisplayName("Consulta todos los estudiantes desde el DAO")
    void mostrarTodosLosEstudiantes_retornaDatosDelDao() {
        List<Estudiante> estudiantes = estudianteService.mostrarTodosLosEstudiantes();

        assertEquals(2, estudiantes.size());
        assertEquals("Jean", estudiantes.get(0).getNombre());
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Estudiante actualizado = new Estudiante(2, "Laura", "Diaz", "laura.actualizada@email.com");

        Estudiante encontrado = estudianteService.obtenerEstudiantePorId(2);
        estudianteService.actualizarEstudiante(actualizado);
        estudianteService.eliminarEstudiante(1);

        assertEquals("Laura", encontrado.getNombre());
        assertSame(actualizado, estudianteDao.estudianteActualizado);
        assertEquals(1, estudianteDao.idEliminado);
    }

    private static class FakeEstudianteDao extends EstudianteDao {
        private final List<Estudiante> estudiantes = new ArrayList<>();
        private Estudiante estudianteGuardado;
        private Estudiante estudianteActualizado;
        private Integer idEliminado;
        private int guardados;

        @Override
        public void guardarEstudiante(Estudiante estudiante) {
            estudianteGuardado = estudiante;
            guardados++;
            estudiantes.add(estudiante);
        }

        @Override
        public List<Estudiante> obtenerTodosLosEstudiantes() {
            return List.copyOf(estudiantes);
        }

        @Override
        public Estudiante obtenerEstudiantePorId(int id) {
            return estudiantes.stream()
                    .filter(estudiante -> estudiante.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void actualizarEstudiante(Estudiante estudiante) {
            estudianteActualizado = estudiante;
        }

        @Override
        public void eliminarEstudiante(int id) {
            idEliminado = id;
        }
    }
}