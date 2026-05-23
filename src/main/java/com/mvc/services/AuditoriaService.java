package com.mvc.services;

import com.mvc.dao.AuditoriaDao;
import com.mvc.models.Auditoria;

import java.util.List;

public class AuditoriaService {

    private static AuditoriaService instancia;

    private final AuditoriaDao auditoriaDao;
    private String usuarioActivo = "sistema";

    private AuditoriaService() {
        this.auditoriaDao = new AuditoriaDao();
    }

    public static AuditoriaService getInstance() {
        if(instancia == null) {
            instancia = new AuditoriaService();
        }

        return instancia;
    }

    public void setUsuarioActivo(String username) {
        this.usuarioActivo = username;
    }

    public void registrar(String accion, String entidad, String descripcion) {
        Auditoria a = new Auditoria(usuarioActivo, accion, entidad, descripcion);
        auditoriaDao.registrar(a);
    }

    public List<Auditoria> listarTodas() {
        return auditoriaDao.listarTodas();
    }
}