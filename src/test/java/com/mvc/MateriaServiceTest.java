package com.mvc;

import com.mvc.dao.MateriaDao;
import com.mvc.models.Materia;
import com.mvc.services.MateriaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MateriaServiceTest {

    private FakeMateriaDao materiaDao;
    private MateriaService materiaService;

    @BeforeEach
    void setUp() {
        materiaDao = new FakeMateriaDao();
        materiaDao.materias.add(new Materia(1, "Bases de Datos", 3));
        materiaDao.materias.add(new Materia(2, "Programacion II", 4));
        materiaService = new MateriaService(materiaDao);
    }

    @Test
    @DisplayName("Registra materias validas delegando en el DAO")
    void registrarMateria_valida_delegaEnDao() {
        Materia materia = new Materia(3, "Calculo", 3);

        materiaService.registrarMateria(materia);

        assertSame(materia, materiaDao.materiaGuardada);
        assertEquals(1, materiaDao.guardadas);
    }

    @Test
    @DisplayName("Rechaza materias sin datos obligatorios y no toca el DAO")
    void registrarMateria_invalida_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(new Materia(3, null, 3))),
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(new Materia(3, "Calculo", null)))
        );

        assertEquals(0, materiaDao.guardadas);
        assertNull(materiaDao.materiaGuardada);
    }

    @Test
    @DisplayName("Consulta todas las materias desde el DAO")
    void mostrarTodasLasMaterias_retornaDatosDelDao() {
        List<Materia> materias = materiaService.mostrarTodasLasMaterias();

        assertEquals(2, materias.size());
        assertEquals("Bases de Datos", materias.get(0).getNombreMateria());
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Materia actualizada = new Materia(2, "Programacion Avanzada", 4);

        Materia encontrada = materiaService.obtenerMateriaPorId(2);
        materiaService.actualizarMateria(actualizada);
        materiaService.eliminarMateria(1);

        assertEquals("Programacion II", encontrada.getNombreMateria());
        assertSame(actualizada, materiaDao.materiaActualizada);
        assertEquals(1, materiaDao.idEliminado);
    }

    private static class FakeMateriaDao extends MateriaDao {
        private final List<Materia> materias = new ArrayList<>();
        private Materia materiaGuardada;
        private Materia materiaActualizada;
        private Integer idEliminado;
        private int guardadas;

        @Override
        public void guardarMateria(Materia materia) {
            materiaGuardada = materia;
            guardadas++;
            materias.add(materia);
        }

        @Override
        public List<Materia> obtenerTodasLasMaterias() {
            return List.copyOf(materias);
        }

        @Override
        public Materia obtenerMateriaPorId(int id) {
            return materias.stream()
                    .filter(materia -> materia.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void actualizarMateria(Materia materia) {
            materiaActualizada = materia;
        }

        @Override
        public void eliminarMateria(int id) {
            idEliminado = id;
        }
    }
}