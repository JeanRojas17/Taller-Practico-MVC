package com.mvc.vista;

import java.util.List;
import java.util.Scanner;

import com.mvc.modelo.Materia;

public class VistaMateria {

    private Scanner scanner;

    public VistaMateria() {
        this.scanner = new Scanner(System.in);
    }

    public Materia solicitarDatosMateria() {
        System.out.println("\nRegistrando los datos de la materia...");

        System.out.print("Ingrese el nombre de la materia: ");
        String nombreMateria = scanner.nextLine();

        System.out.print("Ingrese los créditos de la materia: ");
        Integer creditos = scanner.nextInt();
        scanner.nextLine();

        return new Materia(0, nombreMateria, creditos);
    }

    public void mostrarDetallesMateria(Materia materia) {
        System.out.println("Id: " +materia.getId());
        System.out.println("Nombre: " +materia.getNombreMateria());
        System.out.println("Créditos: " +materia.getCreditos());
    }

    public void mostrarTodasLasMaterias(List<Materia> materias) {
        System.out.println("\nLista de Materias:");

        for(Materia materia : materias) {
            mostrarDetallesMateria(materia);
        }
    }

    public int solicitarIdMateria() {
        System.out.print("\nIngrese el ID de la materia: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public Materia solicitarDatosMateriaActualizados(Materia materiaExistente) {
        System.out.println("Actualizando los datos de la materia...");

        System.out.print("Ingrese el nuevo nombre de la materia: ");
        String nuevoNombre = scanner.nextLine();

        System.out.print("Ingrese los nuevos créditos de la materia: ");
        Integer nuevosCreditos = scanner.nextInt();
        scanner.nextLine();

        return new Materia(materiaExistente.getId(), nuevoNombre, nuevosCreditos);
    }

    public int solicitarIdMateriaParaEliminar() {
        System.out.print("\nIngrese el ID de la materia que desea eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        return id;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}