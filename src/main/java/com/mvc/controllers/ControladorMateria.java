package com.mvc.controllers;

import com.mvc.config.ConfiguracionApp;
import com.mvc.models.Materia;
import com.mvc.services.AuditoriaService;
import com.mvc.services.MateriaService;
import com.mvc.views.VistaMateriaSwing;

import java.util.List;

public class ControladorMateria {

    private final VistaMateriaSwing vista;
    private final MateriaService service;
    private final Runnable notificar;

    public ControladorMateria(VistaMateriaSwing vista, MateriaService service) {
        this(vista, service, null);
    }

    public ControladorMateria(VistaMateriaSwing vista, MateriaService service, Runnable notificar) {
        this.vista = vista;
        this.service = service;
        this.notificar = notificar;

        vista.setOnRegistrar(this::registrar);
        vista.setOnActualizar(this::actualizar);
        vista.setOnEliminar(this::eliminar);
        vista.setOnRefrescar(this::refrescar);

        refrescar();
    }

    private void registrar() {
        String nombreMateria = vista.getNombreMateria();
        String creditosTexto = vista.getCreditosTexto();

        if(nombreMateria.isEmpty() || creditosTexto.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Créditos son obligatorios.");
            return;
        }

        try {
            Integer creditos = Integer.parseInt(creditosTexto);
            Materia nueva = new Materia(0, nombreMateria, creditos);

            service.registrarMateria(nueva);
            AuditoriaService.getInstance().registrar("CREAR", "Materia", "Materia registrada: " +nombreMateria+ " (" +creditosTexto+ " créditos)");
            vista.mostrarMensaje("Materia registrada exitosamente.");
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
        } catch(NumberFormatException ex) {
            vista.mostrarError("El campo Créditos debe ser un número entero.");
        } catch(IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        } catch(Exception ex) {
            vista.mostrarError("Error al registrar: " +ex.getMessage());
        }
    }

    private void actualizar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona una materia de la tabla para actualizar.");
            return;
        }

        String nombreMateria = vista.getNombreMateria();
        String creditosTexto = vista.getCreditosTexto();

        if(nombreMateria.isEmpty() || creditosTexto.isEmpty()) {
            vista.mostrarError("Los campos Nombre y Créditos son obligatorios.");
            return;
        }

        try {
            Integer creditos = Integer.parseInt(creditosTexto);
            Materia actualizada = new Materia(id, nombreMateria, creditos);

            service.actualizarMateria(actualizada);
            AuditoriaService.getInstance().registrar("ACTUALIZAR", "Materia", "Materia ID " +id+ " actualizada: " +nombreMateria);
            vista.mostrarMensaje("Materia actualizada exitosamente.");
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
        } catch(NumberFormatException ex) {
            vista.mostrarError("El campo Créditos debe ser un número entero.");
        } catch(Exception ex) {
            vista.mostrarError("Error al actualizar: " +ex.getMessage());
        }
    }

    private void eliminar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona una materia de la tabla para eliminar.");
            return;
        }

        if(ConfiguracionApp.getInstance().isConfirmarEliminacion()) {
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de que deseas eliminar la materia con ID " +id+ "?",
                "Confirmar eliminación",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
            );

            if (confirmacion != javax.swing.JOptionPane.YES_OPTION) return;
        }

        try {
            service.eliminarMateria(id);
            AuditoriaService.getInstance().registrar("ELIMINAR", "Materia", "Materia ID " +id+ " eliminada.");
            vista.mostrarMensaje("Materia eliminada exitosamente.");
            vista.limpiarCampos();
            refrescar();
            if (notificar != null) notificar.run();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private void refrescar() {
        try {
            List<Materia> materias = service.mostrarTodasLasMaterias();
            vista.cargarMaterias(materias);
        } catch(Exception ex) {
            vista.mostrarError("Error al cargar materias: " +ex.getMessage());
        }
    }
}