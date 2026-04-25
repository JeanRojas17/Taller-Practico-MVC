package com.mvc.controlador;

import com.mvc.modelo.Docente;
import com.mvc.servicios.DocenteService;
import com.mvc.vista.VistaDocente;

public class ControladorDocente {

    private VistaDocente vistaDocente;
    private DocenteService docenteService;

    public ControladorDocente(VistaDocente vistaDocente, DocenteService docenteService) {
        this.vistaDocente = vistaDocente;
        this.docenteService = docenteService;
    }

    public void registrarDocente() {
        Docente nuevoDocente = vistaDocente.solicitarDatosDocente();

        docenteService.registrarDocente(nuevoDocente);
        vistaDocente.mostrarMensaje("Docente registrado exitosamente.");
    }

    public void mostrarTodosLosDocentes() {
        vistaDocente.mostrarTodosLosDocentes(docenteService.mostrarTodosLosDocentes());
    }

    public void mostrarDetallesDocente(int id) {
        Docente docente = docenteService.obtenerDocentePorId(id);

        if(docente != null) {
            vistaDocente.mostrarDetallesDocente(docente);
        } else {
            vistaDocente.mostrarMensaje("No se encontró un docente con el ID proporcionado.");
        }
    }

    public void actualizarDocente() {
        int id = vistaDocente.solicitarIdDocente();
        Docente docenteExistente = docenteService.obtenerDocentePorId(id);

        if(docenteExistente != null) {
            Docente docenteActualizado = vistaDocente.solicitarDatosDocenteActualizados(docenteExistente);
            docenteService.actualizarDocente(docenteActualizado);
            vistaDocente.mostrarMensaje("Docente actualizado exitosamente.");
        } else {
            vistaDocente.mostrarMensaje("No se encontró un docente con el ID proporcionado.");
        }
    }

    public void eliminarDocente() {
        int id = vistaDocente.solicitarIdDocenteParaEliminar();
        docenteService.eliminarDocente(id);
        vistaDocente.mostrarMensaje("Docente eliminado exitosamente.");
    }
}