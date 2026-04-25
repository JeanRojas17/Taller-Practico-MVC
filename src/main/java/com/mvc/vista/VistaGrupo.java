package com.mvc.vista;

import java.util.List;
import java.util.Scanner;

import com.mvc.modelo.Grupo;

public class VistaGrupo {

    private Scanner scanner;

    public VistaGrupo() {
        this.scanner = new Scanner(System.in);
    }

    public String[] solicitarDatosGrupo() {
        System.out.println("\nRegistrando los datos del grupo...");

        System.out.print("Ingrese el ID de la materia: ");
        String idMateria = scanner.nextLine();

        System.out.print("Ingrese el ID del docente: ");
        String idDocente = scanner.nextLine();

        System.out.print("Ingrese el aula: ");
        String aula = scanner.nextLine();

        System.out.print("Ingrese el horario: ");
        String horario = scanner.nextLine();

        return new String[]{idMateria, idDocente, aula, horario};
    }

    public void mostrarDetallesGrupo(Grupo grupo) {
        System.out.println("Id Grupo: " +grupo.getId());
        System.out.println("Materia: " +grupo.getMateria().getNombreMateria()+ " (" +grupo.getMateria().getCreditos()+ " créditos)");
        System.out.println("Docente: " +grupo.getDocente().getNombre()+ " - " +grupo.getDocente().getEspecialidad());
        System.out.println("Aula: " +grupo.getAula());
        System.out.println("Horario: " +grupo.getHorario());
    }

    public void mostrarTodosLosGrupos(List<Grupo> grupos) {
        System.out.println("\nLista de Grupos:");

        for(Grupo grupo : grupos) {
            mostrarDetallesGrupo(grupo);
            System.out.println("-------------------");
        }
    }

    public int solicitarIdGrupo() {
        System.out.print("\nIngrese el ID del grupo: ");
        int id = Integer.parseInt(scanner.nextLine());
        return id;
    }

    public String[] solicitarDatosGrupoActualizados() {
        System.out.println("Actualizando los datos del grupo...");

        System.out.print("Ingrese el nuevo ID de la materia: ");
        String idMateria = scanner.nextLine();

        System.out.print("Ingrese el nuevo ID del docente: ");
        String idDocente = scanner.nextLine();

        System.out.print("Ingrese el nuevo aula: ");
        String aula = scanner.nextLine();

        System.out.print("Ingrese el nuevo horario: ");
        String horario = scanner.nextLine();

        return new String[]{idMateria, idDocente, aula, horario};
    }

    public int solicitarIdGrupoParaEliminar() {
        System.out.print("\nIngrese el ID del grupo que desea eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        return id;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}