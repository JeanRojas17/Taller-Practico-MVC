package com.mvc;

import com.mvc.models.Materia;
import com.mvc.services.MateriaService;
import com.mvc.dao.MateriaDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MateriaServiceTest {

    private MateriaService materiaService;

    @BeforeEach
    void setUp() {
        materiaService = new MateriaService(new MateriaDao());
    }

    @Test
    @DisplayName("Registrar materia con datos válidos no lanza excepción")
    void registrarMateria_datosValidos_noLanzaExcepcion() {
        Materia materia = new Materia(0, "Bases de Datos", 3);
        assertDoesNotThrow(() -> materiaService.registrarMateria(materia));
    }

    @Test
    @DisplayName("Registrar materia nula lanza IllegalArgumentException")
    void registrarMateria_nula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> materiaService.registrarMateria(null));
    }

    @Test
    @DisplayName("Registrar materia sin nombre lanza IllegalArgumentException")
    void registrarMateria_sinNombre_lanzaExcepcion() {
        Materia materia = new Materia(0, null, 3);
        assertThrows(IllegalArgumentException.class,
                () -> materiaService.registrarMateria(materia));
    }

    @Test
    @DisplayName("Registrar materia sin créditos lanza IllegalArgumentException")
    void registrarMateria_sinCreditos_lanzaExcepcion() {
        Materia materia = new Materia(0, "Bases de Datos", null);
        assertThrows(IllegalArgumentException.class,
                () -> materiaService.registrarMateria(materia));
    }
}