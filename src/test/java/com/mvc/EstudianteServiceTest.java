package com.mvc;

import com.mvc.models.Estudiante;
import com.mvc.services.EstudianteService;
import com.mvc.dao.EstudianteDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteServiceTest {

    private EstudianteService estudianteService;

    @BeforeEach
    void setUp() {
        estudianteService = new EstudianteService(new EstudianteDao());
    }

    @Test
    @DisplayName("Registrar estudiante con datos válidos no lanza excepción")
    void registrarEstudiante_datosValidos_noLanzaExcepcion() {
        Estudiante estudiante = new Estudiante(0, "Jean", "Rojas", "jean@email.com");
        assertDoesNotThrow(() -> estudianteService.registrarEstudiante(estudiante));
    }

    @Test
    @DisplayName("Registrar estudiante nulo lanza IllegalArgumentException")
    void registrarEstudiante_nulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> estudianteService.registrarEstudiante(null));
    }

    @Test
    @DisplayName("Registrar estudiante sin nombre lanza IllegalArgumentException")
    void registrarEstudiante_sinNombre_lanzaExcepcion() {
        Estudiante estudiante = new Estudiante(0, null, "Rojas", "jean@email.com");
        assertThrows(IllegalArgumentException.class,
                () -> estudianteService.registrarEstudiante(estudiante));
    }

    @Test
    @DisplayName("Registrar estudiante sin apellido lanza IllegalArgumentException")
    void registrarEstudiante_sinApellido_lanzaExcepcion() {
        Estudiante estudiante = new Estudiante(0, "Jean", null, "jean@email.com");
        assertThrows(IllegalArgumentException.class,
                () -> estudianteService.registrarEstudiante(estudiante));
    }
}