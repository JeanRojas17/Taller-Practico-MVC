package com.mvc.services;

import java.util.List;

import com.mvc.dao.EstudianteDao;
import com.mvc.models.Estudiante;

public class EstudianteService {

    private static final String REGEX_EMAIL = "^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

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

        validarEmail(estudiante.getCorreo());

        estudianteDao.guardarEstudiante(estudiante);
    }

    public List<Estudiante> mostrarTodosLosEstudiantes() {
        return estudianteDao.obtenerTodosLosEstudiantes();
    }

    public Estudiante obtenerEstudiantePorId(int id) {
        return estudianteDao.obtenerEstudiantePorId(id);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        if(estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }

        validarEmail(estudiante.getCorreo());

        estudianteDao.actualizarEstudiante(estudiante);
    }

    public void eliminarEstudiante(int id) {
        estudianteDao.eliminarEstudiante(id);
    }

    private void validarEmail(String email) {
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }

        if(!email.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido. Ejemplo: nombre@dominio.com");
        }
    }
}