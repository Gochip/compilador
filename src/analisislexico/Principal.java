package analisislexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parisi Germán
 * @version 1.0
 */
public class Principal {

    public static void main(String args[]) {
        TablaTransicionesEstados tabla = new TablaTransicionesEstados();
        cargarDesdeArchivo(tabla);
        /*tabla.cargarEstado("0");
         tabla.cargarEstado("1");
         tabla.cargarEstado("2");
         tabla.cargarEstado("3");
         tabla.cargarEstado("4");
         tabla.cargarEstado("5");
         tabla.cargarTerminal("0..9");
         tabla.cargarEstadoAceptacion("C");
         tabla.cargarTransicion("A", "1", "B", true);
         tabla.cargarTransicion("A", "0", "C", true);
         tabla.cargarTransicion("B", "1", "C", true);
         tabla.cargarTransicion("B", "0", "B", true);
         tabla.cargarTransicion("C", "0", "C", true);
         tabla.cargarTransicion("C", "1", "B", true);*/
//        tabla.mostrar();

        String cadena = "(\n"
                + "R(2)R(3)\n"
                + "2";
        cadena += "\n";
        int indiceCadena = 0;
        while (indiceCadena < cadena.length()) {
            String estado = "0";
            char terminal = cadena.charAt(indiceCadena);
            while (!tabla.esEstadoAceptacion(estado)) {
                String nuevoEstado = tabla.obtenerSiguienteEstado(estado, terminal + "");
                if (nuevoEstado == null) {
                    // Estoy en estado de error...
                    indiceCadena++;
                    estado = nuevoEstado;
                    break;
                }

                if (tabla.avanzarEnLectura(estado, terminal + "")) {
                    indiceCadena++;
                    if (indiceCadena < cadena.length()) {
                        terminal = cadena.charAt(indiceCadena);
                    } else {
                        estado = nuevoEstado;
                        break;
                    }
                }
                estado = nuevoEstado;
            }
            //System.out.println("Leído: " + token + " , Estado: " + estado);
            if(estado == null){
                System.out.println("TOKEN MAL FORMADO");
            }else if (tabla.esEstadoAceptacion(estado)) {
                System.out.println(tabla.obtenerToken(estado));
            } else {
                System.out.println("IGNORADO");
            }
        }
    }

    private static void detectarToken(String estado) {

    }

    private static void cargarDesdeArchivo(TablaTransicionesEstados tabla) {
        try {
            Scanner sc = new Scanner(new File("afd.txt"));

            // Terminales
            String linea1 = sc.nextLine();
            String partes[] = linea1.split(":");
            String terminales[] = partes[1].split(";");
            for (int i = 0; i < terminales.length; i++) {
                tabla.cargarTerminal(terminales[i]);
            }

            // Estados
            String linea2 = sc.nextLine();
            partes = linea2.split(":");
            String estados[] = partes[1].split(";");
            for (int i = 0; i < estados.length; i++) {
                tabla.cargarEstado(estados[i]);
            }

            // Estados de aceptación
            String linea3 = sc.nextLine();
            partes = linea3.split(":");
            String estadosAceptacion[] = partes[1].split(";");
            for (int i = 0; i < estadosAceptacion.length; i++) {
                partes = estadosAceptacion[i].split(",");
                tabla.cargarEstadoAceptacion(partes[0], partes[1]);
            }

            // Transiciones
            sc.nextLine();
            while (sc.hasNextLine()) {
                String linea4 = sc.nextLine();
                if (!linea4.trim().equals("")) {
                    String transiciones[] = linea4.split(",");
                    boolean avanza = !transiciones[1].contains("[");
                    transiciones[1] = transiciones[1].replace("[", "").replace("]", "");
                    tabla.cargarTransicion(transiciones[0], transiciones[1], transiciones[2], avanza);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
