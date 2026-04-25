package com.mvc.vista;

import java.util.List;
import java.util.Scanner;

import com.mvc.modelo.Docente;

public class VistaDocente {

    private Scanner scanner;

    public VistaDocente() {
        this.scanner = new Scanner(System.in);
    }

    public Docente solicitarDatosDocente() {
        System.out.println("\nRegistrando los datos del docente...");

        System.out.print("Ingrese el nombre del docente: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese la especialidad del docente: ");
        String especialidad = scanner.nextLine();

        return new Docente(0, nombre, especialidad);
    }

    public void mostrarDetallesDocente(Docente docente) {
        System.out.println("Id: " +docente.getId());
        System.out.println("Nombre: " +docente.getNombre());
        System.out.println("Especialidad: " +docente.getEspecialidad());
    }

    public void mostrarTodosLosDocentes(List<Docente> docentes) {
        System.out.println("\nLista de Docentes:");

        for(Docente docente : docentes) {
            mostrarDetallesDocente(docente);
        }
    }

    public int solicitarIdDocente() {
        System.out.print("\nIngrese el ID del docente: ");
        return scanner.nextInt();
    }

    public Docente solicitarDatosDocenteActualizados(Docente docenteExistente) {
        System.out.println("Actualizando los datos del docente...");

        scanner.nextLine();

        System.out.print("Ingrese el nuevo nombre del docente: ");
        String nuevoNombre = scanner.nextLine();

        System.out.print("Ingrese la nueva especialidad del docente: ");
        String nuevaEspecialidad = scanner.nextLine();

        return new Docente(docenteExistente.getId(), nuevoNombre, nuevaEspecialidad);
    }

    public int solicitarIdDocenteParaEliminar() {
        System.out.print("\nIngrese el ID del docente que desea eliminar: ");
        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}