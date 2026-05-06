package com.mvc;

import com.mvc.dao.GrupoDao;
import com.mvc.models.Docente;
import com.mvc.models.Grupo;
import com.mvc.models.Materia;
import com.mvc.services.GrupoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrupoServiceTest {

    private FakeGrupoDao grupoDao;
    private GrupoService grupoService;
    private Materia materia;
    private Docente docente;

    @BeforeEach
    void setUp() {
        grupoDao = new FakeGrupoDao();
        materia = new Materia(1, "Programacion II", 3);
        docente = new Docente(1, "Ana Torres", "Software");
        grupoDao.grupos.add(new Grupo(1, materia, docente, "Aula 101", "Lunes 8am"));
        grupoService = new GrupoService(grupoDao);
    }

    @Test
    @DisplayName("Registra grupos validos delegando en el DAO")
    void registrarGrupo_valido_delegaEnDao() {
        Grupo grupo = new Grupo(2, materia, docente, "Aula 202", "Martes 10am");

        grupoService.registrarGrupo(grupo);

        assertSame(grupo, grupoDao.grupoGuardado);
        assertEquals(1, grupoDao.guardados);
    }

    @Test
    @DisplayName("Rechaza grupos incompletos y no toca el DAO")
    void registrarGrupo_invalido_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(new Grupo(2, null, docente, "Aula 202", "Martes 10am"))),
                () -> assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(new Grupo(2, materia, null, "Aula 202", "Martes 10am"))),
                () -> assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(new Grupo(2, materia, docente, null, "Martes 10am"))),
                () -> assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(new Grupo(2, materia, docente, "Aula 202", null)))
        );

        assertEquals(0, grupoDao.guardados);
        assertNull(grupoDao.grupoGuardado);
    }

    @Test
    @DisplayName("Consulta todos los grupos desde el DAO")
    void mostrarTodosLosGrupos_retornaDatosDelDao() {
        List<Grupo> grupos = grupoService.mostrarTodosLosGrupos();

        assertEquals(1, grupos.size());
        assertEquals("Programacion II", grupos.get(0).getMateria().getNombreMateria());
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Grupo actualizado = new Grupo(1, materia, docente, "Aula 303", "Viernes 2pm");

        Grupo encontrado = grupoService.obtenerGrupoPorId(1);
        grupoService.actualizarGrupo(actualizado);
        grupoService.eliminarGrupo(1);

        assertEquals("Aula 101", encontrado.getAula());
        assertSame(actualizado, grupoDao.grupoActualizado);
        assertEquals(1, grupoDao.idEliminado);
    }

    private static class FakeGrupoDao extends GrupoDao {
        private final List<Grupo> grupos = new ArrayList<>();
        private Grupo grupoGuardado;
        private Grupo grupoActualizado;
        private Integer idEliminado;
        private int guardados;

        @Override
        public void guardarGrupo(Grupo grupo) {
            grupoGuardado = grupo;
            guardados++;
            grupos.add(grupo);
        }

        @Override
        public List<Grupo> obtenerTodosLosGrupos() {
            return List.copyOf(grupos);
        }

        @Override
        public Grupo obtenerGrupoPorId(int id) {
            return grupos.stream()
                    .filter(grupo -> grupo.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void actualizarGrupo(Grupo grupo) {
            grupoActualizado = grupo;
        }

        @Override
        public void eliminarGrupo(int id) {
            idEliminado = id;
        }
    }
}