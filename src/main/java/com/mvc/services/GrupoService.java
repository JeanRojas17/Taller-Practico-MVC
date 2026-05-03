package com.mvc.services;

import java.util.List;

import com.mvc.dao.GrupoDao;
import com.mvc.models.Grupo;

public class GrupoService {

    private final GrupoDao grupoDao;

    public GrupoService(GrupoDao grupoDao) {
        this.grupoDao = grupoDao;
    }

    public void registrarGrupo(Grupo grupo) {
        if(grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo.");
        }
        if(grupo.getMateria() == null || grupo.getDocente() == null) {
            throw new IllegalArgumentException("El grupo debe tener una materia y un docente asignados.");
        }
        if(grupo.getAula() == null || grupo.getHorario() == null) {
            throw new IllegalArgumentException("Los campos aula y horario del grupo son obligatorios.");
        }
        
        grupoDao.guardarGrupo(grupo);
    }

    public List<Grupo> mostrarTodosLosGrupos() {
        return grupoDao.obtenerTodosLosGrupos();
    }

    public Grupo obtenerGrupoPorId(int id) {
        return grupoDao.obtenerGrupoPorId(id);
    }

    public void actualizarGrupo(Grupo grupo) {
        grupoDao.actualizarGrupo(grupo);
    }

    public void eliminarGrupo(int id) {
        grupoDao.eliminarGrupo(id);
    }
}