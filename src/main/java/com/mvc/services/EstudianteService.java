package com.mvc.services;

import java.util.List;

import com.mvc.dao.EstudianteDao;
import com.mvc.models.Estudiante;

public class EstudianteService {

    private final EstudianteDao estudianteDao;

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {

        if(estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        if(estudiante.getNombre() == null || estudiante.getApellido() == null) {
            throw new IllegalArgumentException("Los campos nombre y apellido del estudiante son obligatorios.");
        }

        estudianteDao.guardarEstudiante(estudiante);
    }

    public List<Estudiante> mostrarTodosLosEstudiantes() {
        return estudianteDao.obtenerTodosLosEstudiantes();
    }

    public Estudiante obtenerEstudiantePorId(int id) {
        return estudianteDao.obtenerEstudiantePorId(id);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        estudianteDao.actualizarEstudiante(estudiante);
    }

    public void eliminarEstudiante(int id) {
        estudianteDao.eliminarEstudiante(id);
    }
}