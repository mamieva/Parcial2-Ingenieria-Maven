package com.ulp;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de aplicación (fachada) para la gestión de semáforos del municipio.
 * Centraliza las operaciones de alto nivel sobre el dominio.
 *
 * Su API se irá completando a medida que avancen las consignas del parcial; por
 * ahora mantiene el registro de los semáforos del sistema.
 */
public class GestionSemaforosService {

    private final List<Semaforo> semaforos = new ArrayList<>();

    public GestionSemaforosService() {
    }

    /** Registra un semáforo en el sistema. */
    public void registrarSemaforo(Semaforo s) {
        semaforos.add(s);
    }

    /**
     * Asigna una orden de composición a una denuncia. Si la denuncia ya tenía
     * una orden asignada, propaga {@link OrdenYaAsignadaException} (una denuncia
     * se liga a una única orden).
     */
    public void asignarOrden(Denuncia denuncia, OrdenDeComposicion orden) {
        denuncia.asignarOrden(orden);
    }

    public List<Semaforo> getSemaforos() {
        return semaforos;
    }

    /**
     * Contador histórico: cuántas denuncias se registraron a lo largo del tiempo
     * sobre el semáforo identificado por su número. Si el semáforo no está
     * registrado, devuelve 0.
     */
    public int contarDenunciasHistoricas(int nroSemaforo) {
        for (Semaforo s : semaforos) {
            if (s.getNro() == nroSemaforo) {
                return s.getHistorialDenuncias().size();
            }
        }
        return 0;
    }
}
