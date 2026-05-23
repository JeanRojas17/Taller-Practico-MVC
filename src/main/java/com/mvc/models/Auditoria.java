package com.mvc.models;

import java.time.LocalDateTime;

public class Auditoria {

    private Integer id;
    private String usuario;
    private String accion;
    private String entidad;
    private String descripcion;
    private LocalDateTime fechaHora;

    public Auditoria() {
    }

    public Auditoria(String usuario, String accion, String entidad, String descripcion) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidad = entidad;
        this.descripcion = descripcion;
    }

    public Integer getId() { 
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}