package com.mvc.models;

public class Materia {

    private Integer id;
    private String nombreMateria;
    private Integer creditos;

    public Materia() {
    }

    public Materia(Integer id, String nombreMateria, Integer creditos) {
        this.id = id;
        this.nombreMateria = nombreMateria;
        this.creditos = creditos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }
}
