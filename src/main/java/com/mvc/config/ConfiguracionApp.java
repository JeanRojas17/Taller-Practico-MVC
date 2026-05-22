package com.mvc.config;

public class ConfiguracionApp {

    private static final ConfiguracionApp INSTANCIA = new ConfiguracionApp();

    private boolean confirmarEliminacion = true;

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
}