package com.mvc.services;

import com.mvc.dao.EstudianteDao;
import com.mvc.models.Estudiante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private EstudianteDao estudianteDao;

    private EstudianteService estudianteService;

    @BeforeEach
    void setUp() {
        estudianteService = new EstudianteService(estudianteDao);
    }

    @Test
    @DisplayName("Registra estudiantes validos delegando en el DAO")
    void registrarEstudiante_valido_delegaEnDao() {
        Estudiante estudiante = new Estudiante(3, "Sofia", "Lopez", "sofia@email.com");

        estudianteService.registrarEstudiante(estudiante);

        verify(estudianteDao).guardarEstudiante(estudiante);
        verifyNoMoreInteractions(estudianteDao);
    }

    @Test
    @DisplayName("Rechaza estudiantes sin datos obligatorios y no toca el DAO")
    void registrarEstudiante_invalido_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(new Estudiante(3, null, "Lopez", "sofia@email.com"))),
                () -> assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(new Estudiante(3, "Sofia", null, "sofia@email.com")))
        );

        verifyNoInteractions(estudianteDao);
    }

    @Test
    @DisplayName("Consulta todos los estudiantes desde el DAO")
    void mostrarTodosLosEstudiantes_retornaDatosDelDao() {
        List<Estudiante> estudiantesEsperados = List.of(
                new Estudiante(1, "Jean", "Rojas", "jean@email.com"),
                new Estudiante(2, "Laura", "Diaz", "laura@email.com")
        );
        when(estudianteDao.obtenerTodosLosEstudiantes()).thenReturn(estudiantesEsperados);

        List<Estudiante> estudiantes = estudianteService.mostrarTodosLosEstudiantes();

        assertSame(estudiantesEsperados, estudiantes);
        verify(estudianteDao).obtenerTodosLosEstudiantes();
        verifyNoMoreInteractions(estudianteDao);
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Estudiante encontrado = new Estudiante(2, "Laura", "Diaz", "laura@email.com");
        Estudiante actualizado = new Estudiante(2, "Laura", "Diaz", "laura.actualizada@email.com");
        when(estudianteDao.obtenerEstudiantePorId(2)).thenReturn(encontrado);

        Estudiante resultado = estudianteService.obtenerEstudiantePorId(2);
        estudianteService.actualizarEstudiante(actualizado);
        estudianteService.eliminarEstudiante(1);

        assertSame(encontrado, resultado);
        verify(estudianteDao).obtenerEstudiantePorId(2);
        verify(estudianteDao).actualizarEstudiante(actualizado);
        verify(estudianteDao).eliminarEstudiante(1);
        verifyNoMoreInteractions(estudianteDao);
    }
}