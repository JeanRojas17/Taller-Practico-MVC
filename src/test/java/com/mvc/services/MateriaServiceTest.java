package com.mvc.services;

import com.mvc.dao.MateriaDao;
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
class MateriaServiceTest {

    @Mock
    private MateriaDao materiaDao;

    private MateriaService materiaService;

    @BeforeEach
    void setUp() {
        materiaService = new MateriaService(materiaDao);
    }

    @Test
    @DisplayName("Registra materias validas delegando en el DAO")
    void registrarMateria_valida_delegaEnDao() {
        Materia materia = new Materia(3, "Calculo", 3);

        materiaService.registrarMateria(materia);

        verify(materiaDao).guardarMateria(materia);
        verifyNoMoreInteractions(materiaDao);
    }

    @Test
    @DisplayName("Rechaza materias sin datos obligatorios y no toca el DAO")
    void registrarMateria_invalida_noDelegaEnDao() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(new Materia(3, null, 3))),
                () -> assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(new Materia(3, "Calculo", null)))
        );

        verifyNoInteractions(materiaDao);
    }

    @Test
    @DisplayName("Consulta todas las materias desde el DAO")
    void mostrarTodasLasMaterias_retornaDatosDelDao() {
        List<Materia> materiasEsperadas = List.of(
                new Materia(1, "Bases de Datos", 3),
                new Materia(2, "Programacion II", 4)
        );
        when(materiaDao.obtenerTodasLasMaterias()).thenReturn(materiasEsperadas);

        List<Materia> materias = materiaService.mostrarTodasLasMaterias();

        assertSame(materiasEsperadas, materias);
        verify(materiaDao).obtenerTodasLasMaterias();
        verifyNoMoreInteractions(materiaDao);
    }

    @Test
    @DisplayName("Consulta, actualiza y elimina delegando en el DAO")
    void operacionesBasicas_deleganEnDao() {
        Materia encontrada = new Materia(2, "Programacion II", 4);
        Materia actualizada = new Materia(2, "Programacion Avanzada", 4);
        when(materiaDao.obtenerMateriaPorId(2)).thenReturn(encontrada);

        Materia resultado = materiaService.obtenerMateriaPorId(2);
        materiaService.actualizarMateria(actualizada);
        materiaService.eliminarMateria(1);

        assertSame(encontrada, resultado);
        verify(materiaDao).obtenerMateriaPorId(2);
        verify(materiaDao).actualizarMateria(actualizada);
        verify(materiaDao).eliminarMateria(1);
        verifyNoMoreInteractions(materiaDao);
    }
}