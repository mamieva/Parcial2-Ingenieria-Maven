package com.ulp;

import java.util.ArrayList;
import java.util.List;

/**
 * Cuadrilla técnica que realiza las reparaciones. Está compuesta por sus
 * miembros y uno de ellos actúa como responsable (líder).
 */
public class EquipoDeControl {

    public static final String LIBRE = "LIBRE";
    public static final String OCUPADO = "OCUPADO";

    private String codigo;
    private String especialidad;
    private String estado;

    // Composición: la cuadrilla y su líder
    private final List<Miembro> miembros = new ArrayList<>();
    private Miembro responsable;

    public EquipoDeControl(String codigo, String especialidad, List<Miembro> miembros, int idxResp) {
        this.codigo = codigo;
        this.especialidad = especialidad;
        this.miembros.addAll(miembros);
        this.responsable = miembros.get(idxResp);
        this.estado = LIBRE;
    }

    /** Pone el equipo a trabajar: queda ocupado y sus miembros, no libres. */
    public void ocuparEquipo() {
        this.estado = OCUPADO;
        for (Miembro m : miembros) {
            m.setEstaLibre(false);
        }
    }

    /** Libera el equipo: vuelve a estar disponible junto con sus miembros. */
    public void liberarEquipo() {
        this.estado = LIBRE;
        for (Miembro m : miembros) {
            m.setEstaLibre(true);
        }
    }

    public String getEstado() {
        return estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Miembro getResponsable() {
        return responsable;
    }

    public List<Miembro> getMiembros() {
        return miembros;
    }
}
