package com.mvc.services;

import com.mvc.dao.GrupoDao;
import com.mvc.models.Docente;
import com.mvc.models.Grupo;
import com.mvc.models.Materia;

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
class GrupoServiceTest {

    @Mock
    private GrupoDao grupoDao;

    private GrupoService grupoService;
    private Materia materia;
    private Docente docente;

    @BeforeEach
    void setUp() {
        grupoService = new GrupoService(grupoDao);
        materia = new Materia(1, "Programacion II", 3);
        docente = new Docente(1, "Ana Torres", "Software");
    }

    @Test
    @DisplayName("Registra grupos validos delegando en el DAO")
    void registrarGrupo_valido_delegaEnDao() {
        Grupo grupo = new Grupo(2, materia, docente, "Aula 202", "Martes 10am");

        grupoService.registrarGrupo(grupo);

        verify(grupoDao).guardarGrupo(grupo);
        verifyNoMoreInteractions(grupoDao);
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

        verifyNoInteractions(grupoDao);
    }

    @Test
    @DisplayName("Consulta todos los grupos desde el DAO")
    void mostrarTodosLosGrupos_retornaDatosDelDao() {
        List<Grupo> gruposEsperados = List.of(new Grupo(1, materia, docente, "Aula 101", "Lunes 8am"));
        when(grupoDao.obtenerTodosLosGrupos()).thenReturn(gruposEsperados);

        List<Grupo> grupos = grupoService.mostrarTodosLosGrupos();

        assertSame(gruposEsperados, grupos);
        verify(grupoDao).obtenerTodosLosGrupos();
        verifyNoMoreInteractions(grupoDao);
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Grupo encontrado = new Grupo(1, materia, docente, "Aula 101", "Lunes 8am");
        Grupo actualizado = new Grupo(1, materia, docente, "Aula 303", "Viernes 2pm");
        when(grupoDao.obtenerGrupoPorId(1)).thenReturn(encontrado);

        Grupo resultado = grupoService.obtenerGrupoPorId(1);
        grupoService.actualizarGrupo(actualizado);
        grupoService.eliminarGrupo(1);

        assertSame(encontrado, resultado);
        verify(grupoDao).obtenerGrupoPorId(1);
        verify(grupoDao).actualizarGrupo(actualizado);
        verify(grupoDao).eliminarGrupo(1);
        verifyNoMoreInteractions(grupoDao);
    }
}