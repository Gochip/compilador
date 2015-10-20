package er;

import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Parisi Germán
 */
public class ER {

    public static void main2(String args[]) {
        Arbol arbol = new Arbol();
        arbol.nodoRaiz = new Nodo('a');
        arbol.nodoRaiz.nodoIzq = new Nodo('b');
        arbol.nodoRaiz.nodoDer = new Nodo('c');
        arbol.nodoRaiz.nodoIzq.nodoIzq = new Nodo('d');
        System.out.println(arbol);
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        String er = sc.nextLine();
        Stack<Arbol> pila = new Stack<>();
        Arbol arbolPrincipal = new Arbol();
        Arbol arbolActual = arbolPrincipal;
        Nodo nodo;
        for (int i = 0; i < er.length(); i++) {
            char c = er.charAt(i);
            switch (c) {
                case '(':
                    pila.push(arbolActual);
                    Arbol subArbol = new Arbol();
                    arbolActual = subArbol;
                    break;
                case ')':
                    Arbol arbol = pila.pop();
                    if(arbol.nodoActual == null){
                        arbol.nodoRaiz = arbolActual.nodoRaiz;
                        arbol.nodoActual = arbol.nodoRaiz;
                    }else{
                        arbol.nodoActual.setNodoDer(arbolActual.nodoRaiz);
                        arbol.nodoActual = arbolActual.nodoRaiz;
                    }
                    arbolActual = arbol;
                    break;
                case '*':
                    arbolActual.nodoActual.cierre = true;
                    break;
                case '.':
                    nodo = new Nodo(c);
                    Nodo aux = arbolActual.nodoActual;
                    if (arbolActual.nodoRaiz == arbolActual.nodoActual) {
                        arbolActual.nodoRaiz = nodo;
                    } else {
                        arbolActual.nodoActual.nodoPadre.setNodoDer(nodo);
                    }
                    nodo.setNodoIzq(aux);
                    arbolActual.nodoActual = nodo;
                    break;
                case '+':
                    while (arbolActual.nodoActual.nodoPadre != null) {
                        arbolActual.nodoActual = arbolActual.nodoActual.nodoPadre;
                    }
                    nodo = new Nodo(c);
                    nodo.setNodoIzq(arbolActual.nodoActual);
                    arbolActual.nodoActual = arbolActual.nodoActual.nodoPadre;
                    arbolActual.nodoRaiz = nodo;
                    break;
                default:
                    nodo = new Nodo(c);
                    if (arbolActual.nodoActual == null) {
                        arbolActual.nodoRaiz = nodo;
                        arbolActual.nodoActual = arbolActual.nodoRaiz;
                    } else {
                        // Se pone a la derecha del actual.
                        arbolActual.nodoActual.setNodoDer(nodo);
                        arbolActual.nodoActual = nodo;
                    }
            }
        }
        System.out.println(arbolPrincipal);
    }
}

class Arbol {

    Nodo nodoRaiz;
    Nodo nodoActual;

    public String recorridoPostOrden(Nodo p, int nivel) {
        if (p == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= nivel; i++) {
            sb.append("=");
        }
        sb.append(">");
        sb.append(String.valueOf(p.c)).append(" (").append(p.cierre).append(")").append("\n");
        sb.append(recorridoPostOrden(p.nodoIzq, nivel + 1));
        sb.append(recorridoPostOrden(p.nodoDer, nivel + 1));
        return sb.toString();
    }

    @Override
    public String toString() {
        Nodo p = nodoRaiz;
        StringBuilder sb = new StringBuilder("Nodo actual: ");
        sb.append(nodoActual.c).append("\n");
        sb.append(recorridoPostOrden(p, 0));
        return sb.toString();
    }
}

class Nodo {

    char c;
    boolean cierre;
    Nodo nodoIzq;
    Nodo nodoDer;
    Nodo nodoPadre;

    public void setNodoIzq(Nodo nodoIzq) {
        this.nodoIzq = nodoIzq;
        nodoIzq.nodoPadre = this;
    }

    public void setNodoDer(Nodo nodoDer) {
        this.nodoDer = nodoDer;
        nodoDer.nodoPadre = this;
    }

    public Nodo(char c) {
        this.c = c;
    }
}
