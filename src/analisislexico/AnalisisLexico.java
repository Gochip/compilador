package analisislexico;

/**
 *
 * @author Parisi Germán
 * @version 1.0
 */
public interface AnalisisLexico {

    public String obtenerSiguienteEstado(String estado, String terminal);

    public boolean esEstadoAceptacion(String estado);

    public boolean esEstadoError(String estado);
    
    public boolean avanzarEnLectura(String estado, String terminal);

    public String obtenerToken(String estado);
    
    public void cargarEstado(String estado);
    
    public void cargarEstadoError(String estado);
    
    public void cargarEstadoAceptacion(String estado, String tokenDevuelto);

    public void cargarTerminal(String terminal);

    public void cargarTransicion(String estado, String terminal, String otroEstado, boolean avanzaEnLectura);
}
