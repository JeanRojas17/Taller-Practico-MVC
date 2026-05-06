package com.mvc;

import com.mvc.dao.DocenteDao;
import com.mvc.models.Docente;
import com.mvc.services.DocenteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocenteServiceTest {

    private FakeDocenteDao docenteDao;
    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        docenteDao = new FakeDocenteDao();
        docenteDao.docentes.add(new Docente(1, "Ana Torres", "Bases de Datos"));
        docenteDao.docentes.add(new Docente(2, "Luis Perez", "Programacion"));
        docenteService = new DocenteService(docenteDao);
    }

    @Test
    @DisplayName("Registra docentes validos delegando en el DAO")
    void registrarDocente_valido_delegaEnDao() {
        Docente docente = new Docente(3, "Marta Gomez", "Calculo");

        docenteService.registrarDocente(docente);

        assertSame(docente, docenteDao.docenteGuardado);
        assertEquals(1, docenteDao.guardados);
    }

    @Test
    @DisplayName("Rechaza docentes sin datos obligatorios y no toca el DAO")
    void registrarDocente_invalido_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(new Docente(3, null, "Calculo"))),
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(new Docente(3, "Marta Gomez", null)))
        );

        assertEquals(0, docenteDao.guardados);
        assertNull(docenteDao.docenteGuardado);
    }

    @Test
    @DisplayName("Consulta todos los docentes desde el DAO")
    void mostrarTodosLosDocentes_retornaDatosDelDao() {
        List<Docente> docentes = docenteService.mostrarTodosLosDocentes();

        assertEquals(2, docentes.size());
        assertEquals("Ana Torres", docentes.get(0).getNombre());
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Docente actualizado = new Docente(2, "Luis Perez", "Arquitectura");

        Docente encontrado = docenteService.obtenerDocentePorId(2);
        docenteService.actualizarDocente(actualizado);
        docenteService.eliminarDocente(1);

        assertEquals("Luis Perez", encontrado.getNombre());
        assertSame(actualizado, docenteDao.docenteActualizado);
        assertEquals(1, docenteDao.idEliminado);
    }

    private static class FakeDocenteDao extends DocenteDao {
        private final List<Docente> docentes = new ArrayList<>();
        private Docente docenteGuardado;
        private Docente docenteActualizado;
        private Integer idEliminado;
        private int guardados;

        @Override
        public void guardarDocente(Docente docente) {
            docenteGuardado = docente;
            guardados++;
            docentes.add(docente);
        }

        @Override
        public List<Docente> obtenerTodosLosDocentes() {
            return List.copyOf(docentes);
        }

        @Override
        public Docente obtenerDocentePorId(int id) {
            return docentes.stream()
                    .filter(docente -> docente.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void actualizarDocente(Docente docente) {
            docenteActualizado = docente;
        }

        @Override
        public void eliminarDocente(int id) {
            idEliminado = id;
        }
    }
}