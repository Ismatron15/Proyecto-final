package aquaguard.data;

import aquaguard.model.Familia;

/**
 * Lista Enlazada Simple para gestionar Familias beneficiarias
 * Estructura de Datos Lineal - Módulo 3: Distribución
 *
 * Permite registrar, consultar, modificar y eliminar familias.
 * El algoritmo de distribución recorre la lista priorizando
 * familias vulnerables.
 */
public class ListaFamilias {

    // Nodo de la lista
    private static class Nodo {
        Familia familia;
        Nodo siguiente;

        Nodo(Familia familia) {
            this.familia = familia;
        }
    }

    private Nodo cabeza;
    private int tamanio;

    public ListaFamilias() {
        cabeza = null;
        tamanio = 0;
    }

    // ---------- Agregar al final ----------
    public void agregar(Familia familia) {
        Nodo nuevo = new Nodo(familia);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null)
                actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    // ---------- Buscar por ID ----------
    public Familia buscar(String idFamilia) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.familia.getIdFamilia().equals(idFamilia))
                return actual.familia;
            actual = actual.siguiente;
        }
        return null;
    }

    // ---------- Eliminar por ID ----------
    public boolean eliminar(String idFamilia) {
        if (cabeza == null) return false;

        if (cabeza.familia.getIdFamilia().equals(idFamilia)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.familia.getIdFamilia().equals(idFamilia)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    // ---------- Listar todas ----------
    public java.util.List<Familia> listarTodas() {
        java.util.List<Familia> lista = new java.util.ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            lista.add(actual.familia);
            actual = actual.siguiente;
        }
        return lista;
    }

    // ---------- Algoritmo de Distribución Equitativa ----------
    /**
     * Distribuye el agua disponible entre las familias.
     * Prioridad: familias vulnerables reciben el doble de litros base.
     *
     * @param litrosDisponibles litros totales a distribuir
     * @return resumen de la distribución
     */
    public String distribuirAgua(double litrosDisponibles) {
        if (tamanio == 0) return "No hay familias registradas.";

        // Paso 1: calcular cuántas "porciones" se necesitan
        // Vulnerable = 2 porciones, Normal = 1 porción
        int totalPorciones = 0;
        Nodo actual = cabeza;
        while (actual != null) {
            totalPorciones += actual.familia.isEsVulnerable() ? 2 : 1;
            actual = actual.siguiente;
        }

        double litrosPorPorcion = litrosDisponibles / totalPorciones;

        // Paso 2: asignar cuotas
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== DISTRIBUCIÓN DE AGUA ===\n");
        resumen.append(String.format("Total disponible: %.1f litros\n", litrosDisponibles));
        resumen.append(String.format("Litros por porción: %.2f\n\n", litrosPorPorcion));

        actual = cabeza;
        while (actual != null) {
            Familia f = actual.familia;
            double cuota = f.isEsVulnerable() ? litrosPorPorcion * 2 : litrosPorPorcion;
            f.setCuotaAsignada(cuota);
            resumen.append(String.format("• %s (%s): %.2f L/día (%.2f L/persona)\n",
                    f.getJefeFamilia(),
                    f.isEsVulnerable() ? "VULNERABLE" : "Normal",
                    cuota,
                    f.calcularConsumoPerCapita()));
            actual = actual.siguiente;
        }
        return resumen.toString();
    }

    public int getTamanio() { return tamanio; }

    public boolean estaVacia() { return tamanio == 0; }
}
