package com.mvc.controlador;

import com.mvc.modelo.Docente;
import com.mvc.modelo.Grupo;
import com.mvc.modelo.Materia;
import com.mvc.servicios.DocenteService;
import com.mvc.servicios.GrupoService;
import com.mvc.servicios.MateriaService;
import com.mvc.vista.VistaGrupoSwing;

import java.util.List;

public class ControladorGrupo {

    private final VistaGrupoSwing vista;
    private final GrupoService grupoService;
    private final MateriaService materiaService;
    private final DocenteService docenteService;

    public ControladorGrupo(VistaGrupoSwing vista, GrupoService grupoService, MateriaService materiaService, DocenteService docenteService) {
        this.vista = vista;
        this.grupoService = grupoService;
        this.materiaService = materiaService;
        this.docenteService = docenteService;

        vista.setOnRegistrar(this::registrar);
        vista.setOnActualizar(this::actualizar);
        vista.setOnEliminar(this::eliminar);
        vista.setOnRefrescar(this::refrescar);

        refrescar();
    }

    private void registrar() {
        String idMateriaTexto = vista.getIdMateriaTexto();
        String idDocenteTexto = vista.getIdDocenteTexto();
        String aula = vista.getAula();
        String horario = vista.getHorario();

        if(idMateriaTexto.isEmpty() || idDocenteTexto.isEmpty() || aula.isEmpty() || horario.isEmpty()) {
            vista.mostrarError("Los campos ID Materia, ID Docente, Aula y Horario son obligatorios.");
            return;
        }

        try {
            Materia materia = obtenerMateria(idMateriaTexto);
            if(materia == null) return;

            Docente docente = obtenerDocente(idDocenteTexto);
            if(docente == null) return;

            Grupo nuevo = new Grupo(0, materia, docente, aula, horario);

            grupoService.registrarGrupo(nuevo);
            vista.mostrarMensaje("Grupo registrado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(NumberFormatException ex) {
            vista.mostrarError("Los campos ID Materia e ID Docente deben ser números enteros.");
        } catch(IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        } catch(Exception ex) {
            vista.mostrarError("Error al registrar: " +ex.getMessage());
        }
    }

    private void actualizar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona un grupo de la tabla para actualizar.");
            return;
        }

        String idMateriaTexto = vista.getIdMateriaTexto();
        String idDocenteTexto = vista.getIdDocenteTexto();
        String aula = vista.getAula();
        String horario = vista.getHorario();

        if(idMateriaTexto.isEmpty() || idDocenteTexto.isEmpty() || aula.isEmpty() || horario.isEmpty()) {
            vista.mostrarError("Los campos ID Materia, ID Docente, Aula y Horario son obligatorios.");
            return;
        }

        try {
            Materia materia = obtenerMateria(idMateriaTexto);
            if(materia == null) return;

            Docente docente = obtenerDocente(idDocenteTexto);
            if(docente == null) return;

            Grupo actualizado = new Grupo(id, materia, docente, aula, horario);

            grupoService.actualizarGrupo(actualizado);
            vista.mostrarMensaje("Grupo actualizado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(NumberFormatException ex) {
            vista.mostrarError("Los campos ID Materia e ID Docente deben ser números enteros.");
        } catch(Exception ex) {
            vista.mostrarError("Error al actualizar: " +ex.getMessage());
        }
    }

    private void eliminar() {
        int id = vista.getIdSeleccionado();

        if(id < 0) {
            vista.mostrarError("Selecciona un grupo de la tabla para eliminar.");
            return;
        }

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            vista,
            "Estás seguro de que deseas eliminar el grupo con ID " +id+ "?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if(confirmacion != javax.swing.JOptionPane.YES_OPTION) return;

        try {
            grupoService.eliminarGrupo(id);
            vista.mostrarMensaje("Grupo eliminado exitosamente.");
            vista.limpiarCampos();
            refrescar();
        } catch(Exception ex) {
            vista.mostrarError("Error al eliminar: " +ex.getMessage());
        }
    }

    private Materia obtenerMateria(String idMateriaTexto) {
        int idMateria = Integer.parseInt(idMateriaTexto);
        Materia materia = materiaService.obtenerMateriaPorId(idMateria);

        if(materia == null) {
            vista.mostrarError("No se encontró una materia con el ID proporcionado.");
        }

        return materia;
    }

    private Docente obtenerDocente(String idDocenteTexto) {
        int idDocente = Integer.parseInt(idDocenteTexto);
        Docente docente = docenteService.obtenerDocentePorId(idDocente);

        if(docente == null) {
            vista.mostrarError("No se encontró un docente con el ID proporcionado.");
        }

        return docente;
    }

    private void refrescar() {
        try {
            List<Grupo> grupos = grupoService.mostrarTodosLosGrupos();
            vista.cargarGrupos(grupos);
        } catch(Exception ex) {
            vista.mostrarError("Error al cargar grupos: " +ex.getMessage());
        }
    }
}