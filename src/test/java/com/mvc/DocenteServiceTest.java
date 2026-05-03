package com.mvc;

import com.mvc.models.Docente;
import com.mvc.services.DocenteService;
import com.mvc.dao.DocenteDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocenteServiceTest {

    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        docenteService = new DocenteService(new DocenteDao());
    }

    @Test
    @DisplayName("Registrar docente con datos válidos no lanza excepción")
    void registrarDocente_datosValidos_noLanzaExcepcion() {
        Docente docente = new Docente(0, "Gabriel", "Ingeniería de Software");
        assertDoesNotThrow(() -> docenteService.registrarDocente(docente));
    }

    @Test
    @DisplayName("Registrar docente nulo lanza IllegalArgumentException")
    void registrarDocente_nulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> docenteService.registrarDocente(null));
    }

    @Test
    @DisplayName("Registrar docente sin nombre lanza IllegalArgumentException")
    void registrarDocente_sinNombre_lanzaExcepcion() {
        Docente docente = new Docente(0, null, "Ingeniería de Software");
        assertThrows(IllegalArgumentException.class,
                () -> docenteService.registrarDocente(docente));
    }

    @Test
    @DisplayName("Registrar docente sin especialidad lanza IllegalArgumentException")
    void registrarDocente_sinEspecialidad_lanzaExcepcion() {
        Docente docente = new Docente(0, "Gabriel", null);
        assertThrows(IllegalArgumentException.class,
                () -> docenteService.registrarDocente(docente));
    }
}