package com.mvc.controlador;

import com.mvc.modelo.Materia;
import com.mvc.servicios.MateriaService;
import com.mvc.vista.VistaMateria;

public class ControladorMateria {

    private VistaMateria vistaMateria;
    private MateriaService materiaService;

    public ControladorMateria(VistaMateria vistaMateria, MateriaService materiaService) {
        this.vistaMateria = vistaMateria;
        this.materiaService = materiaService;
    }

    public void registrarMateria() {
        Materia nuevaMateria = vistaMateria.solicitarDatosMateria();

        materiaService.registrarMateria(nuevaMateria);
        vistaMateria.mostrarMensaje("Materia registrada exitosamente.");
    }

    public void mostrarTodasLasMaterias() {
        vistaMateria.mostrarTodasLasMaterias(materiaService.mostrarTodasLasMaterias());
    }

    public void mostrarDetallesMateria(int id) {
        Materia materia = materiaService.obtenerMateriaPorId(id);

        if(materia != null) {
            vistaMateria.mostrarDetallesMateria(materia);
        } else {
            vistaMateria.mostrarMensaje("No se encontró una materia con el ID proporcionado.");
        }
    }

    public void actualizarMateria() {
        int id = vistaMateria.solicitarIdMateria();
        Materia materiaExistente = materiaService.obtenerMateriaPorId(id);

        if(materiaExistente != null) {
            Materia materiaActualizada = vistaMateria.solicitarDatosMateriaActualizados(materiaExistente);
            materiaService.actualizarMateria(materiaActualizada);
            vistaMateria.mostrarMensaje("Materia actualizada exitosamente.");
        } else {
            vistaMateria.mostrarMensaje("No se encontró una materia con el ID proporcionado.");
        }
    }

    public void eliminarMateria() {
        int id = vistaMateria.solicitarIdMateriaParaEliminar();
        materiaService.eliminarMateria(id);
        vistaMateria.mostrarMensaje("Materia eliminada exitosamente.");
    }
}