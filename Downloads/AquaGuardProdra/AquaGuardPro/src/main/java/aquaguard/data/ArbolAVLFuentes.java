package aquaguard.data;

import aquaguard.model.FuenteAgua;

/**
 * Árbol AVL para gestionar las Fuentes de Agua
 * Estructura de Datos No Lineal - Módulos 1 y 2
 *
 * Ordenado por: idFuente (String)
 */
public class ArbolAVLFuentes {

    // Nodo interno del árbol
    private static class Nodo {
        FuenteAgua fuente;
        Nodo izq, der;
        int altura;

        Nodo(FuenteAgua fuente) {
            this.fuente = fuente;
            this.altura = 1;
        }
    }

    private Nodo raiz;

    // ---------- Métodos de altura y balance ----------

    private int altura(Nodo n) {
        return (n == null) ? 0 : n.altura;
    }

    private int factorBalance(Nodo n) {
        return (n == null) ? 0 : altura(n.izq) - altura(n.der);
    }

    private void actualizarAltura(Nodo n) {
        n.altura = 1 + Math.max(altura(n.izq), altura(n.der));
    }

    // ---------- Rotaciones ----------

    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izq;
        Nodo T2 = x.der;
        x.der = y;
        y.izq = T2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.der;
        Nodo T2 = y.izq;
        y.izq = x;
        x.der = T2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    private Nodo rebalancear(Nodo n) {
        actualizarAltura(n);
        int balance = factorBalance(n);

        // Caso izquierda-izquierda
        if (balance > 1 && factorBalance(n.izq) >= 0)
            return rotarDerecha(n);

        // Caso izquierda-derecha
        if (balance > 1 && factorBalance(n.izq) < 0) {
            n.izq = rotarIzquierda(n.izq);
            return rotarDerecha(n);
        }

        // Caso derecha-derecha
        if (balance < -1 && factorBalance(n.der) <= 0)
            return rotarIzquierda(n);

        // Caso derecha-izquierda
        if (balance < -1 && factorBalance(n.der) > 0) {
            n.der = rotarDerecha(n.der);
            return rotarIzquierda(n);
        }

        return n;
    }

    // ---------- Insertar ----------

    public void insertar(FuenteAgua fuente) {
        raiz = insertar(raiz, fuente);
    }

    private Nodo insertar(Nodo nodo, FuenteAgua fuente) {
        if (nodo == null) return new Nodo(fuente);

        int cmp = fuente.getIdFuente().compareTo(nodo.fuente.getIdFuente());
        if (cmp < 0)
            nodo.izq = insertar(nodo.izq, fuente);
        else if (cmp > 0)
            nodo.der = insertar(nodo.der, fuente);
        else
            nodo.fuente = fuente; // actualiza si ya existe

        return rebalancear(nodo);
    }

    // ---------- Buscar ----------

    public FuenteAgua buscar(String idFuente) {
        Nodo resultado = buscar(raiz, idFuente);
        return (resultado != null) ? resultado.fuente : null;
    }

    private Nodo buscar(Nodo nodo, String id) {
        if (nodo == null) return null;
        int cmp = id.compareTo(nodo.fuente.getIdFuente());
        if (cmp < 0) return buscar(nodo.izq, id);
        if (cmp > 0) return buscar(nodo.der, id);
        return nodo;
    }

    // ---------- Eliminar (baja lógica) ----------

    public void eliminar(String idFuente) {
        FuenteAgua f = buscar(idFuente);
        if (f != null) {
            f.setActiva(false); // baja lógica, no se borra del árbol
        }
    }

    // ---------- Listar en orden (recorrido in-order) ----------

    public java.util.List<FuenteAgua> listarTodas() {
        java.util.List<FuenteAgua> lista = new java.util.ArrayList<>();
        inOrder(raiz, lista, false);
        return lista;
    }

    public java.util.List<FuenteAgua> listarSoloActivas() {
        java.util.List<FuenteAgua> lista = new java.util.ArrayList<>();
        inOrder(raiz, lista, true);
        return lista;
    }

    private void inOrder(Nodo nodo, java.util.List<FuenteAgua> lista, boolean soloActivas) {
        if (nodo == null) return;
        inOrder(nodo.izq, lista, soloActivas);
        if (!soloActivas || nodo.fuente.isActiva())
            lista.add(nodo.fuente);
        inOrder(nodo.der, lista, soloActivas);
    }

    public int tamanio() {
        return listarTodas().size();
    }
}
