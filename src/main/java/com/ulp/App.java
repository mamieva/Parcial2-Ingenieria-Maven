package com.ulp;

import java.util.Date;
import java.util.List;

/**
 * Pequeña demostración del flujo completo del sistema de semáforos.
 */
public class App {
    public static void main(String[] args) {
        // 1) Un semáforo (nace con sus 3 luces por composición)
        Semaforo s = new Semaforo(101, "FALLANDO", "Av. Siempreviva y Calle 1", "220V");
        System.out.println("Semáforo " + s.getNro() + " con " + s.getLuces().size() + " luces.");

        // 2) Un denunciante reporta una falla
        Denunciante juan = new Denunciante("Juan Pérez", "juan@mail.com");
        Denuncia d = new Denuncia("D-001", new Date(), "Av. Siempreviva", "Calle 1",
                "Luz roja quemada", "ALTA", juan, s);
        System.out.println("Denuncias del semáforo: " + d.listDenunciaPorSemaforo(101).size());

        // 3) Se arma una cuadrilla técnica
        List<Miembro> cuadrilla = List.of(
                new Miembro("M1", "Ana", "Técnica"),
                new Miembro("M2", "Beto", "Electricista"),
                new Miembro("M3", "Caro", "Ayudante"),
                new Miembro("M4", "Diego", "Chofer"));
        EquipoDeControl equipo = new EquipoDeControl("EQ-01", "Electricidad", cuadrilla, 0);

        // 4) Orden de trabajo ligada a la denuncia
        OrdenDeComposicion orden = new OrdenDeComposicion(5001, new Date(), "Reemplazo de luz roja", d);
        orden.asignarEquipo(equipo);
        System.out.println("Equipo tras asignar: " + equipo.getEstado());

        // 5) Se completa la reparación
        orden.registrarReparacionCompletada(new Date());
        System.out.println("Equipo tras completar: " + equipo.getEstado());
        System.out.println("Reparaciones totales del semáforo: " + s.getCantidadTotalReparaciones());
    }
}
