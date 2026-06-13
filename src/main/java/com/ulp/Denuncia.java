package com.ulp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Denuncia de una falla en un semáforo, hecha por un Denunciante.
 * Queda asociada de forma bidireccional al semáforo y al denunciante.
 */
public class Denuncia {

    // Registro general de todas las denuncias del sistema (para las búsquedas)
    private static final List<Denuncia> TODAS = new ArrayList<>();

    // Niveles de prioridad válidos que reconoce la Municipalidad
    private static final List<String> PRIORIDADES_VALIDAS = List.of("Alta", "Media", "Baja");

    private String codD;
    private Date fechaDenuncia;
    private String calleX;
    private String calleY;
    private String problema;
    private String prioridadReparacion;

    private Denunciante denunciante;
    private Semaforo semaforoAsociado;
    private OrdenDeComposicion ordenAsignada; // 0..1: se setea al crear la orden

    public Denuncia(String codD, Date fecha, String calleX, String calleY,
                    String problema, String prioridad, Denunciante d, Semaforo s) {
        this.codD = codD;
        this.fechaDenuncia = fecha;
        this.calleX = calleX;
        this.calleY = calleY;
        this.problema = problema;
        this.prioridadReparacion = prioridad;
        this.denunciante = d;
        this.semaforoAsociado = s;

        // Enlaza ambos lados de la relación automáticamente
        d.registrarDenuncia(this);
        s.agregarDenunciaHistorial(this);

        TODAS.add(this);
    }

    public Semaforo getSemaforoAsociado() {
        return semaforoAsociado;
    }

    public Denunciante getDenunciante() {
        return denunciante;
    }

    /**
     * Liga una orden a esta denuncia. La llama el constructor de
     * OrdenDeComposicion. Si la denuncia ya tenía una orden, lanza
     * OrdenYaAsignadaException (multiplicidad "1" del UML).
     */
    void asignarOrden(OrdenDeComposicion orden) {
        if (ordenAsignada != null) {
            throw new OrdenYaAsignadaException(
                    "La denuncia " + codD + " ya tiene asignada la orden #"
                            + ordenAsignada.getNroDeOrden() + ".");
        }
        ordenAsignada = orden;
    }

    public OrdenDeComposicion getOrdenAsignada() {
        return ordenAsignada;
    }

    /** Devuelve todas las denuncias hechas sobre un semáforo dado. */
    public List<Denuncia> listDenunciaPorSemaforo(int nroSemaforo) {
        List<Denuncia> resultado = new ArrayList<>();
        for (Denuncia den : TODAS) {
            if (den.semaforoAsociado.getNro() == nroSemaforo) {
                resultado.add(den);
            }
        }
        return resultado;
    }

    /** Devuelve todas las denuncias hechas por un denunciante (por su mail). */
    public List<Denuncia> listDenunciaPorDenunciante(String mail) {
        List<Denuncia> resultado = new ArrayList<>();
        for (Denuncia den : TODAS) {
            if (den.denunciante.getMail().equals(mail)) {
                resultado.add(den);
            }
        }
        return resultado;
    }

    /** Limpia el registro global (útil para aislar pruebas). */
    static void reiniciarRegistro() {
        TODAS.clear();
    }

    public String getCodD() {
        return codD;
    }

    public Date getFechaDenuncia() {
        return fechaDenuncia;
    }

    public String getProblema() {
        return problema;
    }

    public String getPrioridadReparacion() {
        return prioridadReparacion;
    }

    /**
     * Indica si la prioridad de la denuncia es uno de los tres niveles estrictos
     * que reconoce el sistema ("Alta", "Media" o "Baja"). Cualquier otro valor se
     * considera inválido. La comparación ignora mayúsculas/minúsculas.
     */
    public boolean esPrioridadValida() {
        return prioridadReparacion != null
                && PRIORIDADES_VALIDAS.stream().anyMatch(prioridadReparacion::equalsIgnoreCase);
    }

    public String getCalleX() {
        return calleX;
    }

    public String getCalleY() {
        return calleY;
    }
}
