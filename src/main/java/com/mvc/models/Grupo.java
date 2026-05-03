package com.mvc.models;

public class Grupo {

    private Integer id;
    private Materia materia;
    private Docente docente;
    private String aula;
    private String horario;

    public Grupo() {
    }

    public Grupo(Integer id, Materia materia, Docente docente, String aula, String horario) {
        this.id = id;
        this.materia = materia;
        this.docente = docente;
        this.aula = aula;
        this.horario = horario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}