package com.mvc.vista;

import java.util.List;
import java.util.Scanner;

import com.mvc.modelo.Estudiante;

public class VistaEstudiante {

    private Scanner scanner;

    public VistaEstudiante() {
        this.scanner = new Scanner(System.in);
    }

    public Estudiante solicitarDatosEstudiante() {

        System.out.println("\nRegistrando los datos del estudiante...");

        System.out.print("Ingrese el nombre del estudiante: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el apellido del estudiante: ");
        String apellido = scanner.nextLine();

        System.out.print("Ingrese el correo del estudiante: ");
        String correo = scanner.nextLine();

        return new Estudiante(0, nombre, apellido, correo);
    }

    public void mostrarDetallesEstudiante(Estudiante estudiante) {
        System.out.println("Id: " +estudiante.getId());
        System.out.println("Nombre: " +estudiante.getNombre());
        System.out.println("Apellido: " +estudiante.getApellido());
        System.out.println("Correo: " +estudiante.getCorreo());
    }

    public void mostrarTodosLosEstudiantes(List<Estudiante> estudiantes) {
        System.out.println("\nLista de Estudiantes: ");
        
        for(Estudiante estudiante : estudiantes) {
            mostrarDetallesEstudiante(estudiante);
        }
    }

    public int solicitarIdEstudiante() {
        System.out.print("\nIngrese el ID del estudiante: ");
        return scanner.nextInt();
    }

    public Estudiante solicitarDatosEstudianteActualizados(Estudiante estudianteExistente) {
        System.out.println("Actualizando los datos del estudiante...");

        scanner.nextLine();

        System.out.print("Ingrese el nuevo nombre del estudiante: ");
        String nuevoNombre = scanner.nextLine();

        System.out.print("Ingrese el nuevo apellido del estudiante: ");
        String nuevoApellido = scanner.nextLine();

        System.out.print("Ingrese el nuevo correo del estudiante: ");
        String nuevoCorreo = scanner.nextLine();

        return new Estudiante(estudianteExistente.getId(), nuevoNombre, nuevoApellido, nuevoCorreo);
    }

    public int solicitarIdEstudianteParaEliminar() {
        System.out.print("\nIngrese el ID del estudiante que desea eliminar: ");
        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}