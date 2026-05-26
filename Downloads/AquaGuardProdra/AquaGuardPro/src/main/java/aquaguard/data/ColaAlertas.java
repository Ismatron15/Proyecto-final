package aquaguard.data;

import aquaguard.model.Medicion;

/**
 * Cola de Alertas para el Módulo 2 - Monitoreo
 * Estructura de Datos Lineal: Cola (FIFO)
 *
 * Las alertas se encolan cuando se detectan mediciones críticas
 * y se desencolan cuando son atendidas por el responsable.
 */
public class ColaAlertas {

    // Nodo de la cola
    private static class Nodo {
        Medicion medicion;
        String mensajeAlerta;
        Medicion.NivelAlerta nivel;
        Nodo siguiente;

        Nodo(Medicion medicion) {
            this.medicion = medicion;
            this.nivel = medicion.calcularNivelAlerta();
            this.mensajeAlerta = medicion.getMensajeEstado();
        }
    }

    private Nodo frente;  // primer elemento (el que sale primero)
    private Nodo fin;     // último elemento (el que entró último)
    private int tamanio;

    public ColaAlertas() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    /**
     * Encola una medición si su nivel de alerta es mayor a NORMAL
     * @return true si fue encolada, false si no genera alerta
     */
    public boolean encolar(Medicion medicion) {
        if (medicion.calcularNivelAlerta() == Medicion.NivelAlerta.NORMAL) {
            return false;
        }
        Nodo nuevo = new Nodo(medicion);
        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
        return true;
    }

    /**
     * Desencola la alerta más antigua (la que lleva más tiempo sin atenderse)
     */
    public Medicion desencolar() {
        if (estaVacia()) return null;
        Medicion m = frente.medicion;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return m;
    }

    /**
     * Ver la próxima alerta sin eliminarla
     */
    public Medicion verFrente() {
        return estaVacia() ? null : frente.medicion;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    /**
     * Obtiene todas las alertas como lista (para mostrar en tabla)
     */
    public java.util.List<String> listarAlertas() {
        java.util.List<String> lista = new java.util.ArrayList<>();
        Nodo actual = frente;
        int pos = 1;
        while (actual != null) {
            lista.add("#" + pos + " | " + actual.nivel + " | "
                    + actual.medicion.getIdFuente() + " | "
                    + actual.mensajeAlerta.replaceAll("[^\\x00-\\x7F]", "") // quita emojis para la lista
                    + " | Fecha: " + actual.medicion.getFecha());
            actual = actual.siguiente;
            pos++;
        }
        return lista;
    }
}
