package aquaguard.model;

/**
 * Clase modelo para representar una familia beneficiaria
 * Módulo 3 - Gestión de Distribución
 */
public class Familia {

    public enum TipoUso {
        DOMESTICO, AGRICOLA, GANADERO
    }

    // Atributos
    private String idFamilia;
    private String jefeFamilia;
    private String sector;
    private int integrantes;
    private TipoUso tipoUso;
    private double cuotaAsignada;   // litros por día
    private boolean esVulnerable;   // adultos mayores, niños, enfermos

    // Constructor
    public Familia(String idFamilia, String jefeFamilia, String sector,
                   int integrantes, TipoUso tipoUso, boolean esVulnerable) {
        this.idFamilia = idFamilia;
        this.jefeFamilia = jefeFamilia;
        this.sector = sector;
        this.integrantes = integrantes;
        this.tipoUso = tipoUso;
        this.esVulnerable = esVulnerable;
        this.cuotaAsignada = 0;
    }

    /**
     * Calcula el consumo per cápita diario
     */
    public double calcularConsumoPerCapita() {
        if (integrantes == 0) return 0;
        return cuotaAsignada / integrantes;
    }

    // --- Getters y Setters ---
    public String getIdFamilia() { return idFamilia; }
    public String getJefeFamilia() { return jefeFamilia; }
    public void setJefeFamilia(String jefeFamilia) { this.jefeFamilia = jefeFamilia; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public int getIntegrantes() { return integrantes; }
    public void setIntegrantes(int integrantes) { this.integrantes = integrantes; }
    public TipoUso getTipoUso() { return tipoUso; }
    public void setTipoUso(TipoUso tipoUso) { this.tipoUso = tipoUso; }
    public double getCuotaAsignada() { return cuotaAsignada; }
    public void setCuotaAsignada(double cuotaAsignada) { this.cuotaAsignada = cuotaAsignada; }
    public boolean isEsVulnerable() { return esVulnerable; }
    public void setEsVulnerable(boolean esVulnerable) { this.esVulnerable = esVulnerable; }

    @Override
    public String toString() {
        return idFamilia + " - " + jefeFamilia + " | Sector: " + sector
                + " | " + integrantes + " personas";
    }
}
