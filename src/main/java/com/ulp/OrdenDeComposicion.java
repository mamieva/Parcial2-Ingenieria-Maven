package com.ulp;

import java.util.Date;

/**
 * Orden de trabajo para reparar una falla. Nace ligada a una única denuncia y
 * puede tener (o no) un equipo de control asignado durante su ciclo de vida.
 */
public class OrdenDeComposicion {

    private int nroDeOrden;
    private Date fechaReparacionProgramada;
    private Date fechaEfectivaReparacion;
    private String detalle;

    private Denuncia denunciaAsociada;
    private EquipoDeControl equipoAsignado; // 0..1: puede no tener equipo aún

    public OrdenDeComposicion(int nro, Date fProg, String detalle, Denuncia d) {
        this.nroDeOrden = nro;
        this.fechaReparacionProgramada = fProg;
        this.detalle = detalle;
        this.denunciaAsociada = d;
        // La orden nace referenciando su denuncia, pero el enlace inverso
        // (denuncia -> orden) lo realiza explícitamente el servicio al asignarla.
    }

    /** Asigna un equipo a la orden y lo deja ocupado. */
    public void asignarEquipo(EquipoDeControl equipo) {
        this.equipoAsignado = equipo;
        equipo.ocuparEquipo();
    }

    /** Marca la reparación como completada en la fecha indicada. */
    public void registrarReparacionCompletada(Date fechaEf) {
        this.fechaEfectivaReparacion = fechaEf;

        // Libera el equipo (si había uno asignado)
        if (equipoAsignado != null) {
            equipoAsignado.liberarEquipo();
        }
        // Suma una reparación al semáforo afectado por la denuncia
        denunciaAsociada.getSemaforoAsociado().registrarReparacion();

        imprimirOrden();
        enviarEmailNotificacion();
    }

    private void imprimirOrden() {
        System.out.println("Orden #" + nroDeOrden + " - " + detalle
                + " | Reparación efectiva: " + fechaEfectivaReparacion);
    }

    private void enviarEmailNotificacion() {
        System.out.println("Email enviado a: "
                + denunciaAsociada.getDenunciante().getMail());
    }

    public int getNroDeOrden() {
        return nroDeOrden;
    }

    public Date getFechaReparacionProgramada() {
        return fechaReparacionProgramada;
    }

    public Date getFechaEfectivaReparacion() {
        return fechaEfectivaReparacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public Denuncia getDenunciaAsociada() {
        return denunciaAsociada;
    }

    public EquipoDeControl getEquipoAsignado() {
        return equipoAsignado;
    }
}
