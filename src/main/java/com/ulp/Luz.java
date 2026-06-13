package com.ulp;

/**
 * Una de las luces que componen físicamente un Semaforo.
 * Forma parte del semáforo por composición (no existe por fuera de él).
 */
public class Luz {

    private String nroSerie;
    private String empresaFabricadora;
    private String tipo;
    private String color;

    public Luz(String nroSerie, String fabricante, String tipo, String color) {
        this.nroSerie = nroSerie;
        this.empresaFabricadora = fabricante;
        this.tipo = tipo;
        this.color = color;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public String getEmpresaFabricadora() {
        return empresaFabricadora;
    }

    public String getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }
}
