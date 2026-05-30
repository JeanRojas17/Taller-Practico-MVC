package com.mvc.config;

public class ConfiguracionApp {

    private static final ConfiguracionApp INSTANCIA = new ConfiguracionApp();

    private boolean confirmarEliminacion = true;
    private int registrosPorPagina = 10;

    private ConfiguracionApp() {}

    public static ConfiguracionApp getInstance() {
        return INSTANCIA;
    }

    public boolean isConfirmarEliminacion() {
        return confirmarEliminacion;
    }

    public void setConfirmarEliminacion(boolean confirmarEliminacion) {
        this.confirmarEliminacion = confirmarEliminacion;
    }

    public int getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public void setRegistrosPorPagina(int registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }
}