package com.ulp;

/**
 * Integrante de un EquipoDeControl (cuadrilla técnica).
 */
public class Miembro {

    private String id;
    private String nombre;
    private String puesto;
    private boolean estaLibre;

    public Miembro(String id, String nombre, String puesto) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.estaLibre = true; // al crearse, el miembro está disponible
    }

    public void setEstaLibre(boolean estado) {
        this.estaLibre = estado;
    }

    public boolean isEstaLibre() {
        return estaLibre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }
}
