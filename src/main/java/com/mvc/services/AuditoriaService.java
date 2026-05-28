package com.mvc.services;

import com.mvc.dao.AuditoriaDao;
import com.mvc.models.Auditoria;

import java.util.List;

public class AuditoriaService {

    private static final AuditoriaService instancia = new AuditoriaService();

    private final AuditoriaDao auditoriaDao;

    private volatile String usuarioActivo = "sistema";

    private AuditoriaService() {
        this.auditoriaDao = new AuditoriaDao();
    }

    public static AuditoriaService getInstance() {
        return instancia;
    }

    public void setUsuarioActivo(String username) {
        this.usuarioActivo = (username != null && !username.isBlank()) ? username : "sistema";
    }

    public void registrar(String accion, String entidad, String descripcion) {
        String usuario = this.usuarioActivo;
        Auditoria a = new Auditoria(usuario, accion, entidad, descripcion);
        auditoriaDao.registrar(a);
    }

    public List<Auditoria> listarTodas() {
        return auditoriaDao.listarTodas();
    }
}