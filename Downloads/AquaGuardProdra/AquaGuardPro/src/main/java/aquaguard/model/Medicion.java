package aquaguard.model;

/**
 * Clase modelo para representar una medición de calidad del agua
 * Módulo 2 - Monitoreo y Alertas
 */
public class Medicion {

    public enum NivelAlerta {
        NORMAL, AMARILLA, NARANJA, ROJA
    }

    // Atributos
    private String idMedicion;
    private String idFuente;
    private double pH;
    private double turbidez;    // NTU
    private double temperatura; // °C
    private boolean coliformes;
    private String fecha;

    // Constructor
    public Medicion(String idMedicion, String idFuente, double pH,
                    double turbidez, double temperatura, boolean coliformes, String fecha) {
        this.idMedicion = idMedicion;
        this.idFuente = idFuente;
        this.pH = pH;
        this.turbidez = turbidez;
        this.temperatura = temperatura;
        this.coliformes = coliformes;
        this.fecha = fecha;
    }

    /**
     * Determina el nivel de alerta según los parámetros registrados.
     * Estándares OMS simplificados.
     */
    public NivelAlerta calcularNivelAlerta() {
        if (coliformes) return NivelAlerta.ROJA;
        if (pH < 6.0 || pH > 9.0 || turbidez > 10) return NivelAlerta.NARANJA;
        if (pH < 6.5 || pH > 8.5 || turbidez > 5) return NivelAlerta.AMARILLA;
        return NivelAlerta.NORMAL;
    }

    /**
     * Retorna un mensaje descriptivo del estado del agua
     */
    public String getMensajeEstado() {
        switch (calcularNivelAlerta()) {
            case ROJA:    return "⛔ ALERTA ROJA - Agua contaminada. No apta para consumo.";
            case NARANJA: return "🟠 ALERTA NARANJA - Parámetros críticos fuera de rango.";
            case AMARILLA:return "🟡 ALERTA AMARILLA - Parámetros en límite. Revisar.";
            default:      return "✅ NORMAL - Agua dentro de estándares OMS.";
        }
    }

    // --- Getters y Setters ---
    public String getIdMedicion() { return idMedicion; }
    public String getIdFuente() { return idFuente; }
    public double getpH() { return pH; }
    public void setpH(double pH) { this.pH = pH; }
    public double getTurbidez() { return turbidez; }
    public void setTurbidez(double turbidez) { this.turbidez = turbidez; }
    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }
    public boolean isColiformes() { return coliformes; }
    public void setColiformes(boolean coliformes) { this.coliformes = coliformes; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    @Override
    public String toString() {
        return idMedicion + " | Fuente: " + idFuente + " | pH: " + pH
                + " | Turbidez: " + turbidez + " NTU | " + fecha;
    }
}
