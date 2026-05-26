package aquaguard.model;

import java.util.Date;

/**
 * Clase modelo para representar una fuente de agua
 * Módulo 1 - Gestión de Fuentes
 */
public class FuenteAgua {

    // Tipos de fuente como enumeración sencilla
    public enum TipoFuente {
        POZO, RIO, RESERVORIO, LLUVIA, MANANTIAL
    }

    public enum EstadoCalidad {
        BUENA, REGULAR, CONTAMINADA, NO_EVALUADA
    }

    // Atributos
    private String idFuente;
    private String nombre;
    private TipoFuente tipo;
    private String ubicacion;
    private double capacidadLitros;
    private double nivelActual;      // porcentaje 0-100
    private EstadoCalidad estadoCalidad;
    private boolean activa;

    // Constructor
    public FuenteAgua(String idFuente, String nombre, TipoFuente tipo,
                      String ubicacion, double capacidadLitros) {
        this.idFuente = idFuente;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.capacidadLitros = capacidadLitros;
        this.nivelActual = 100.0;
        this.estadoCalidad = EstadoCalidad.NO_EVALUADA;
        this.activa = true;
    }

    // Calcula el porcentaje de disponibilidad
    public double calcularPorcentajeDisponibilidad() {
        return (nivelActual / 100.0) * capacidadLitros;
    }

    // Verifica si la fuente está operativa
    public boolean estaOperativa() {
        return activa && estadoCalidad != EstadoCalidad.CONTAMINADA;
    }

    // --- Getters y Setters ---
    public String getIdFuente() { return idFuente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoFuente getTipo() { return tipo; }
    public void setTipo(TipoFuente tipo) { this.tipo = tipo; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public double getCapacidadLitros() { return capacidadLitros; }
    public void setCapacidadLitros(double capacidadLitros) { this.capacidadLitros = capacidadLitros; }
    public double getNivelActual() { return nivelActual; }
    public void setNivelActual(double nivelActual) { this.nivelActual = nivelActual; }
    public EstadoCalidad getEstadoCalidad() { return estadoCalidad; }
    public void setEstadoCalidad(EstadoCalidad estadoCalidad) { this.estadoCalidad = estadoCalidad; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    @Override
    public String toString() {
        return idFuente + " - " + nombre + " (" + tipo + ")";
    }
}
