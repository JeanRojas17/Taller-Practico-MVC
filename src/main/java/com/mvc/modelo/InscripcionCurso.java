package com.mvc.modelo;

public class InscripcionCurso {

    private Integer id;
    private Estudiante estudiante;
    private Grupo grupo;
    private Float notaFinal;
    private String estado;

    public InscripcionCurso() {
    }

    public InscripcionCurso(Integer id, Estudiante estudiante, Grupo grupo, Float notaFinal, String estado) {
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

    public Float getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(Float notaFinal) {
        this.notaFinal = notaFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}