package com.mvc;

import com.mvc.dao.DocenteDao;
import com.mvc.models.Docente;
import com.mvc.services.DocenteService;

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
class DocenteServiceTest {

    @Mock
    private DocenteDao docenteDao;

    private DocenteService docenteService;

    @BeforeEach
    void setUp() {
        docenteService = new DocenteService(docenteDao);
    }

    @Test
    @DisplayName("Registra docentes validos delegando en el DAO")
    void registrarDocente_valido_delegaEnDao() {
        Docente docente = new Docente(3, "Marta Gomez", "Calculo");

        docenteService.registrarDocente(docente);

        verify(docenteDao).guardarDocente(docente);
        verifyNoMoreInteractions(docenteDao);
    }

    @Test
    @DisplayName("Rechaza docentes sin datos obligatorios y no toca el DAO")
    void registrarDocente_invalido_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(new Docente(3, null, "Calculo"))),
                () -> assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(new Docente(3, "Marta Gomez", null)))
        );

        verifyNoInteractions(docenteDao);
    }

    @Test
    @DisplayName("Consulta todos los docentes desde el DAO")
    void mostrarTodosLosDocentes_retornaDatosDelDao() {
        List<Docente> docentesEsperados = List.of(
                new Docente(1, "Ana Torres", "Bases de Datos"),
                new Docente(2, "Luis Perez", "Programacion")
        );
        when(docenteDao.obtenerTodosLosDocentes()).thenReturn(docentesEsperados);

        List<Docente> docentes = docenteService.mostrarTodosLosDocentes();

        assertSame(docentesEsperados, docentes);
        verify(docenteDao).obtenerTodosLosDocentes();
        verifyNoMoreInteractions(docenteDao);
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Docente encontrado = new Docente(2, "Luis Perez", "Programacion");
        Docente actualizado = new Docente(2, "Luis Perez", "Arquitectura");
        when(docenteDao.obtenerDocentePorId(2)).thenReturn(encontrado);

        Docente resultado = docenteService.obtenerDocentePorId(2);
        docenteService.actualizarDocente(actualizado);
        docenteService.eliminarDocente(1);

        assertSame(encontrado, resultado);
        verify(docenteDao).obtenerDocentePorId(2);
        verify(docenteDao).actualizarDocente(actualizado);
        verify(docenteDao).eliminarDocente(1);
        verifyNoMoreInteractions(docenteDao);
    }
}