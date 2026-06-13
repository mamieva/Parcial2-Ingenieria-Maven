package com.ulp;

import java.util.ArrayList;
import java.util.List;

/**
 * Semáforo municipal. Está compuesto físicamente por 3 luces (composición
 * irreversible) y mantiene el historial de denuncias asociadas a sus fallas.
 */
public class Semaforo {

    private int nro;
    private String estado;
    private String ubicacion;
    private String tipoCorriente;

    // Composición: las 3 luces nacen y mueren con el semáforo
    private final List<Luz> luces = new ArrayList<>();
    // Historial de fallas reportadas sobre este aparato
    private final List<Denuncia> historialDenuncias = new ArrayList<>();
    // Cantidad de reparaciones efectivamente completadas
    private int cantidadReparaciones;

    public Semaforo(int nro, String estado, String ubicacion, String tipoCorriente) {
        this.nro = nro;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.tipoCorriente = tipoCorriente;
        this.cantidadReparaciones = 0;
        crearLuces();
    }

    // Las 3 luces se crean internamente: el semáforo es dueño de ellas
    private void crearLuces() {
        luces.add(new Luz(nro + "-R", "Genérico", "LED", "Rojo"));
        luces.add(new Luz(nro + "-A", "Genérico", "LED", "Amarillo"));
        luces.add(new Luz(nro + "-V", "Genérico", "LED", "Verde"));
    }

    public void agregarDenunciaHistorial(Denuncia d) {
        historialDenuncias.add(d);
    }

    public int getCantidadTotalReparaciones() {
        return cantidadReparaciones;
    }

    // Llamado por la OrdenDeComposicion cuando se completa una reparación
    void registrarReparacion() {
        cantidadReparaciones++;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNro() {
        return nro;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getTipoCorriente() {
        return tipoCorriente;
    }

    public List<Luz> getLuces() {
        return luces;
    }

    public List<Denuncia> getHistorialDenuncias() {
        return historialDenuncias;
    }
}
