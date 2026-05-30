package com.mvc;

import com.mvc.dao.InscripcionCursoDao;
import com.mvc.models.Docente;
import com.mvc.models.Estudiante;
import com.mvc.models.Grupo;
import com.mvc.models.InscripcionCurso;
import com.mvc.models.Materia;
import com.mvc.services.InscripcionCursoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionCursoServiceTest {

    @Mock
    private InscripcionCursoDao inscripcionCursoDao;

    private InscripcionCursoService inscripcionCursoService;
    private Estudiante estudiante;
    private Grupo grupo;

    @BeforeEach
    void setUp() {
        inscripcionCursoService = new InscripcionCursoService(inscripcionCursoDao);
        estudiante = new Estudiante(1, "Jean", "Rojas", "jean@email.com");
        Materia materia = new Materia(1, "Bases de Datos", 3);
        Docente docente = new Docente(1, "Ana Torres", "Software");
        grupo = new Grupo(1, materia, docente, "Aula 101", "Lunes 8am");
    }

    @Test
    @DisplayName("Registra inscripciones validas delegando en el DAO")
    void registrarInscripcion_valida_delegaEnDao() {
        InscripcionCurso inscripcion = new InscripcionCurso(2, estudiante, grupo, null, "Inscrito");

        inscripcionCursoService.registrarInscripcion(inscripcion);

        verify(inscripcionCursoDao).guardarInscripcion(inscripcion);
        verifyNoMoreInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Rechaza inscripciones incompletas y no toca el DAO")
    void registrarInscripcion_invalida_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, null, grupo, null, "Inscrito"))),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, estudiante, null, null, "Inscrito"))),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(new InscripcionCurso(2, estudiante, grupo, null, null)))
        );

        verifyNoInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Rechaza nota fuera del rango 0.0 - 5.0")
    void registrarInscripcion_notaFueraDeRango_lanzaExcepcion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(
                        new InscripcionCurso(2, estudiante, grupo, new BigDecimal("-0.1"), "Aprobado"))),
                () -> assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(
                        new InscripcionCurso(2, estudiante, grupo, new BigDecimal("5.1"), "Aprobado")))
        );

        verifyNoInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Rechaza estado invalido segun presencia de nota")
    void registrarInscripcion_estadoInvalido_lanzaExcepcion() {
        // Estado manual cuando hay nota no es válido
        assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(
                new InscripcionCurso(2, estudiante, grupo, new BigDecimal("4.0"), "Inscrito")));

        // Estado con nota cuando no hay nota no es válido
        assertThrows(IllegalArgumentException.class, () -> inscripcionCursoService.registrarInscripcion(
                new InscripcionCurso(2, estudiante, grupo, null, "Aprobado")));

        verifyNoInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Consulta todas las inscripciones desde el DAO")
    void mostrarTodasLasInscripciones_retornaDatosDelDao() {
        List<InscripcionCurso> inscripcionesEsperadas = List.of(
                new InscripcionCurso(1, estudiante, grupo, new BigDecimal("4.2"), "Aprobado"));
        when(inscripcionCursoDao.obtenerTodasLasInscripciones()).thenReturn(inscripcionesEsperadas);

        List<InscripcionCurso> inscripciones = inscripcionCursoService.mostrarTodasLasInscripciones();

        assertSame(inscripcionesEsperadas, inscripciones);
        verify(inscripcionCursoDao).obtenerTodasLasInscripciones();
        verifyNoMoreInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        InscripcionCurso encontrada = new InscripcionCurso(1, estudiante, grupo, new BigDecimal("4.2"), "Aprobado");
        InscripcionCurso actualizada = new InscripcionCurso(1, estudiante, grupo, new BigDecimal("4.5"), "Aprobado");
        when(inscripcionCursoDao.obtenerInscripcionPorId(1)).thenReturn(encontrada);

        InscripcionCurso resultado = inscripcionCursoService.obtenerInscripcionPorId(1);
        inscripcionCursoService.actualizarInscripcion(actualizada);
        inscripcionCursoService.eliminarInscripcion(1);

        assertSame(encontrada, resultado);
        verify(inscripcionCursoDao).obtenerInscripcionPorId(1);
        verify(inscripcionCursoDao).actualizarInscripcion(actualizada);
        verify(inscripcionCursoDao).eliminarInscripcion(1);
        verifyNoMoreInteractions(inscripcionCursoDao);
    }

    @Test
    @DisplayName("Filtra inscripciones por estudiante y por grupo delegando en el DAO")
    void filtrosPorEstudianteYGrupo_deleganEnDao() {
        List<InscripcionCurso> porEstudianteEsperadas = List.of(
                new InscripcionCurso(1, estudiante, grupo, new BigDecimal("4.2"), "Aprobado"));
        List<InscripcionCurso> porGrupoEsperadas = List.of(
                new InscripcionCurso(2, estudiante, grupo, null, "Inscrito"));
        when(inscripcionCursoDao.obtenerInscripcionesPorEstudiante(1)).thenReturn(porEstudianteEsperadas);
        when(inscripcionCursoDao.obtenerInscripcionesPorGrupo(1)).thenReturn(porGrupoEsperadas);

        List<InscripcionCurso> porEstudiante = inscripcionCursoService.obtenerInscripcionesPorEstudiante(1);
        List<InscripcionCurso> porGrupo = inscripcionCursoService.obtenerInscripcionesPorGrupo(1);

        assertSame(porEstudianteEsperadas, porEstudiante);
        assertSame(porGrupoEsperadas, porGrupo);
        verify(inscripcionCursoDao).obtenerInscripcionesPorEstudiante(1);
        verify(inscripcionCursoDao).obtenerInscripcionesPorGrupo(1);
        verifyNoMoreInteractions(inscripcionCursoDao);
    }
}