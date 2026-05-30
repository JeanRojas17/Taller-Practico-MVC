package com.mvc.models;

import java.math.BigDecimal;

public class InscripcionCurso {

    private Integer id;
    private Estudiante estudiante;
    private Grupo grupo;
    private BigDecimal notaFinal;
    private String estado;

    public InscripcionCurso() {
    }

    public InscripcionCurso(Integer id, Estudiante estudiante, Grupo grupo, BigDecimal notaFinal, String estado) {
        this.id = id;
        this.estudiante = estudiante;
        this.grupo = grupo;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public BigDecimal getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(BigDecimal notaFinal) {
        this.notaFinal = notaFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}