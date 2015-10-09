package analisislexico;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Parisi Germán
 * @version 1.0
 */
public class TablaTransicionesEstados implements AnalisisLexico {

    private final ArrayList<String> estados, terminales;
    private int[][] transiciones;
    private final ArrayList<Boolean> estadosAceptacion;
    private final ArrayList<String> tokenDevueltos;
    private final ArrayList<Boolean> estadosError;
    private boolean[][] avanzarEnLectura;

    public TablaTransicionesEstados() {
        this.estados = new ArrayList<>();
        this.terminales = new ArrayList<>();
        this.estadosAceptacion = new ArrayList<>();
        this.tokenDevueltos = new ArrayList<>();
        this.estadosError = new ArrayList<>();
    }

    @Override
    public void cargarEstado(String estado) {
        estados.add(estado);
        estadosAceptacion.add(false);
        tokenDevueltos.add(null);
        estadosError.add(false);
    }

    @Override
    public void cargarEstadoError(String estado) {
        int indiceEstado = obtenerIndiceEstado(estado);
        estadosError.set(indiceEstado, true);
    }

    @Override
    public void cargarEstadoAceptacion(String estado, String tokenDevuelto) {
        int indiceEstado = obtenerIndiceEstado(estado);
        estadosAceptacion.set(indiceEstado, true);
        tokenDevueltos.set(indiceEstado, tokenDevuelto);
    }

    @Override
    public String obtenerToken(String estado) {
        int indiceEstado = obtenerIndiceEstado(estado);
        return tokenDevueltos.get(indiceEstado);
    }

    private String[] patron(String patron) {
        String resultado[] = null;
        if (patron.contains("otro")) {

        } else if (patron.equals("\\b")) {
            resultado = new String[1];
            resultado[0] = " ";
        } else if (patron.equals("\\t")) {
            resultado = new String[1];
            resultado[0] = "\t";
        } else if (patron.equals("\\n")) {
            resultado = new String[1];
            resultado[0] = "\n";
        } else if (patron.contains("todo")) {
            resultado = new String[terminales.size()];
            for (int i = 0; i < terminales.size(); i++) {
                String t = terminales.get(i);
                resultado[i] = t;
            }
        } else {
            String partes[] = patron.split("\\.\\.");
            char desde = partes[0].charAt(0);
            char hasta = partes[1].charAt(0);
            resultado = new String[hasta - desde + 1];
            for (int i = desde; i <= hasta; i++) {
                char c = (char) i;
                resultado[i - desde] = String.valueOf(c);
            }
        }
        return resultado;
    }

    @Override
    public void cargarTerminal(String terminal) {
        if (terminal.length() > 1) {
            String terminalesString[] = patron(terminal);
            for (String term : terminalesString) {
                terminales.add(term);
            }
        } else {
            terminales.add(terminal);
        }
    }

    @Override
    public String obtenerSiguienteEstado(String estado, String terminal) {
        int otroEstado = transiciones[obtenerIndiceEstado(estado)][obtenerIndiceTerminal(terminal)];
        if(otroEstado < 0){
            return null;
        }else{
            return estados.get(otroEstado);
        }
    }

    @Override
    public boolean esEstadoAceptacion(String estado) {
        int indiceEstado = obtenerIndiceEstado(estado);
        return estadosAceptacion.get(indiceEstado);
    }

    @Override
    public boolean esEstadoError(String estado) {
        int indiceEstado = obtenerIndiceEstado(estado);
        return estadosError.get(indiceEstado);
    }

    @Override
    public boolean avanzarEnLectura(String estado, String terminal) {
        int indiceEstado = obtenerIndiceEstado(estado);
        return avanzarEnLectura[indiceEstado][obtenerIndiceTerminal(terminal)];
    }

    @Override
    public void cargarTransicion(String estado, String terminal, String otroEstado, boolean avanzaEnLectura) {
        if (transiciones == null) {
            transiciones = new int[estados.size()][terminales.size()];
            for (int i = 0; i < transiciones.length; i++) {
                for (int j = 0; j < transiciones[i].length; j++) {
                    transiciones[i][j] = -1;
                }
            }
            avanzarEnLectura = new boolean[estados.size()][terminales.size()];
        }
        int indiceEstado = obtenerIndiceEstado(estado);
        int indiceOtroEstado = obtenerIndiceEstado(otroEstado);
        if (terminal.length() > 1) {
            String terminalesString[] = patron(terminal);
            for (String term : terminalesString) {
                int indiceTerminal = obtenerIndiceTerminal(term);
                transiciones[indiceEstado][indiceTerminal] = indiceOtroEstado;
                avanzarEnLectura[indiceEstado][indiceTerminal] = avanzaEnLectura;
            }
        } else {
            int indiceTerminal = obtenerIndiceTerminal(terminal);
            transiciones[indiceEstado][indiceTerminal] = indiceOtroEstado;
            avanzarEnLectura[indiceEstado][indiceTerminal] = avanzaEnLectura;
        }
    }

    public void mostrar() {
        System.out.println("Estados");
        System.out.println(this.estados);
        System.out.println("Estados de aceptación");
        System.out.println(this.estadosAceptacion);
        System.out.println("Estados de error");
        System.out.println(this.estadosError);
        System.out.println("Terminales");
        System.out.println(this.terminales);
        System.out.println("Transiciones");
        for (int i = 0; i < transiciones.length; i++) {
            for (int j = 0; j < transiciones[i].length; j++) {
                System.out.print(transiciones[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int obtenerIndiceEstado(String estado) {
        return estados.indexOf(estado);
    }

    private int obtenerIndiceTerminal(String terminal) {
        return terminales.indexOf(terminal);
    }

}
