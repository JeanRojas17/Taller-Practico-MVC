package com.mvc.vista;

import java.util.List;
import java.util.Scanner;

import com.mvc.modelo.InscripcionCurso;

public class VistaInscripcionCurso {

    private Scanner scanner;

    public VistaInscripcionCurso() {
        this.scanner = new Scanner(System.in);
    }

    public String[] solicitarDatosInscripcion() {
        System.out.println("\nRegistrando los datos de la inscripción...");

        System.out.print("Ingrese el ID del estudiante: ");
        String idEstudiante = scanner.nextLine();

        System.out.print("Ingrese el ID del grupo: ");
        String idGrupo = scanner.nextLine();

        System.out.print("Ingrese la nota final (deje vacío si aún no tiene nota): ");
        String notaFinal = scanner.nextLine();

        String estado = "";
        if(notaFinal.isBlank()) {
            System.out.println("Seleccione el estado:");
            System.out.println("1. Inscrito");
            System.out.println("2. En curso");
            System.out.println("3. Retirado");

            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch(opcion) {
                case "1":
                    estado = "Inscrito";
                    break;
                case "2":
                    estado = "En curso";
                    break;
                case "3":
                    estado = "Retirado";
                    break;
                default:
                    estado = "Inscrito";
            }
        }

        return new String[]{idEstudiante, idGrupo, notaFinal, estado};
    }

    public void mostrarDetallesInscripcion(InscripcionCurso inscripcion) {
        System.out.println("Id Inscripción: " +inscripcion.getId());
        System.out.println("Estudiante: " +inscripcion.getEstudiante().getNombre()+ " " +inscripcion.getEstudiante().getApellido()+ " (" +inscripcion.getEstudiante().getCorreo()+ ")");
        System.out.println("Grupo: " +inscripcion.getGrupo().getId()+ " - " +inscripcion.getGrupo().getMateria().getNombreMateria()+ " | Aula: " +inscripcion.getGrupo().getAula()+ " | Horario: " +inscripcion.getGrupo().getHorario());
        System.out.println("Docente: " +inscripcion.getGrupo().getDocente().getNombre());
        System.out.println("Nota Final: " +(inscripcion.getNotaFinal() != null ? inscripcion.getNotaFinal() : "Sin nota"));
        System.out.println("Estado: " +inscripcion.getEstado());
    }

    public void mostrarTodasLasInscripciones(List<InscripcionCurso> inscripciones) {
        System.out.println("\nLista de Inscripciones:");

        for(InscripcionCurso inscripcion : inscripciones) {
            mostrarDetallesInscripcion(inscripcion);
            System.out.println("-------------------");
        }
    }

    public int solicitarIdInscripcion() {
        System.out.print("\nIngrese el ID de la inscripción: ");
        int id = Integer.parseInt(scanner.nextLine());
        return id;
    }

    public String[] solicitarDatosInscripcionActualizados() {
        System.out.println("Actualizando los datos de la inscripción...");

        System.out.print("Ingrese el nuevo ID del estudiante: ");
        String idEstudiante = scanner.nextLine();

        System.out.print("Ingrese el nuevo ID del grupo: ");
        String idGrupo = scanner.nextLine();

        System.out.print("Ingrese la nueva nota final (deje vacío si no tiene nota): ");
        String notaFinal = scanner.nextLine();

        String estado = "";
        if(notaFinal.isBlank()) {
            System.out.println("Seleccione el estado:");
            System.out.println("1. Inscrito");
            System.out.println("2. En curso");
            System.out.println("3. Retirado");

            System.out.print("Opción: ");
            String opcion = scanner.nextLine();

            switch(opcion) {
                case "1":
                    estado = "Inscrito";
                    break;
                case "2":
                    estado = "En curso";
                    break;
                case "3":
                    estado = "Retirado";
                    break;
                default:
                    estado = "Inscrito";
            }
        }

        return new String[]{idEstudiante, idGrupo, notaFinal, estado};
    }

    public int solicitarIdInscripcionParaEliminar() {
        System.out.print("\nIngrese el ID de la inscripción que desea eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        return id;
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}