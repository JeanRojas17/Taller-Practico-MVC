package com.mvc;

import com.mvc.controlador.ControladorEstudiante;
import com.mvc.dao.EstudianteDao;
import com.mvc.servicios.EstudianteService;
import com.mvc.vista.VistaEstudiante;

public class Main {

    public static void main(String[] args) {
        System.out.println("------------------------------");
        System.out.println("Práctica MVC - Sistema Académico UNIAJC");
        System.out.println("------------------------------");

        // 1. Crear la instancia de la vista
        VistaEstudiante vistaEstudiante = new VistaEstudiante();

        // 2. Crear la instancia del dao
        EstudianteDao estudianteDao = new EstudianteDao();

        // 3. Crear la instancia del servicio
        EstudianteService estudianteService = new EstudianteService(estudianteDao);

        // 4. Crear la instancia del controlador
        ControladorEstudiante controladorEstudiante = new ControladorEstudiante(vistaEstudiante, estudianteService);

        // Probamos la ejecución del flujo de registro de un estudiante
        controladorEstudiante.mostrarTodosLosEstudiantes();
        controladorEstudiante.registrarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();

        // Probamos la ejecución del flujo de consulta de un estudiante por ID
        int idEstudiante = vistaEstudiante.solicitarIdEstudiante();
        controladorEstudiante.mostrarDetallesEstudiante(idEstudiante);

        // Probamos la ejecución del flujo de actualización de un estudiante por ID
        controladorEstudiante.actualizarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();

        // Probamos la ejecución del flujo de eliminación de un estudiante por ID
        controladorEstudiante.eliminarEstudiante();
        controladorEstudiante.mostrarTodosLosEstudiantes();
    }
}