package com.mvc.config;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionApp {

    private static final ConfiguracionApp INSTANCIA = new ConfiguracionApp();

    private boolean confirmarEliminacion = true;
    private int registrosPorPagina = 10;

    private final List<Runnable> listenersPaginacion = new ArrayList<>();

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
        listenersPaginacion.forEach(Runnable::run);
    }

    public void addListenerPaginacion(Runnable listener) {
        listenersPaginacion.add(listener);
    }

    public void clearListenersPaginacion() {
        listenersPaginacion.clear();
    }
}