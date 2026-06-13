package com.ulp;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Orden de Composición")
public class OrdenDeComposicionTest {

    private Semaforo sem;
    private Denuncia denuncia;
    private EquipoDeControl equipo;

    @BeforeEach
    public void setUp() {
        // Aísla el registro global de denuncias entre pruebas
        Denuncia.reiniciarRegistro();

        sem = new Semaforo(101, "FALLANDO", "Av. 1 y Calle 2", "220V");
        Denunciante juan = new Denunciante("Juan", "juan@mail.com");
        denuncia = new Denuncia("D-1", new Date(), "Av. 1", "Calle 2",
                "Luz roja quemada", "ALTA", juan, sem);

        List<Miembro> miembros = List.of(
                new Miembro("M1", "Ana", "Técnica"),
                new Miembro("M2", "Beto", "Electricista"),
                new Miembro("M3", "Caro", "Ayudante"),
                new Miembro("M4", "Diego", "Chofer"));
        equipo = new EquipoDeControl("EQ-01", "Electricidad", miembros, 0);
    }

    @Test
    @DisplayName("Nace ligada a su denuncia y sin equipo asignado")
    public void testCreacion() {
        OrdenDeComposicion orden = new OrdenDeComposicion(5001, new Date(), "Reemplazo de luz", denuncia);

        assertSame(denuncia, orden.getDenunciaAsociada());
        assertNull(orden.getEquipoAsignado(), "Una orden recién creada no tiene equipo (0..1)");
        assertNull(orden.getFechaEfectivaReparacion(), "Todavía no se reparó");
    }

    @Test
    @DisplayName("asignarEquipo lo guarda en la orden y lo deja OCUPADO")
    public void testAsignarEquipo() {
        OrdenDeComposicion orden = new OrdenDeComposicion(5001, new Date(), "Reemplazo de luz", denuncia);

        orden.asignarEquipo(equipo);

        assertSame(equipo, orden.getEquipoAsignado());
        assertEquals("OCUPADO", equipo.getEstado());
        assertTrue(equipo.getMiembros().stream().noneMatch(Miembro::isEstaLibre));
    }

    @Test
    @DisplayName("registrarReparacionCompletada libera el equipo, suma reparación y fecha")
    public void testReparacionCompletadaConEquipo() {
        OrdenDeComposicion orden = new OrdenDeComposicion(5001, new Date(), "Reemplazo de luz", denuncia);
        orden.asignarEquipo(equipo);

        Date fechaEf = new Date();
        orden.registrarReparacionCompletada(fechaEf);

        // El equipo vuelve a estar disponible
        assertEquals("LIBRE", equipo.getEstado());
        assertTrue(equipo.getMiembros().stream().allMatch(Miembro::isEstaLibre));
        // El semáforo afectado contabiliza la reparación
        assertEquals(1, sem.getCantidadTotalReparaciones());
        // Queda registrada la fecha efectiva
        assertSame(fechaEf, orden.getFechaEfectivaReparacion());
    }

    @Test
    @DisplayName("registrarReparacionCompletada funciona aunque no haya equipo asignado (0..1)")
    public void testReparacionCompletadaSinEquipo() {
        OrdenDeComposicion orden = new OrdenDeComposicion(5002, new Date(), "Inspección menor", denuncia);

        Date fechaEf = new Date();
        // No debe lanzar excepción aunque equipoAsignado sea null
        assertDoesNotThrow(() -> orden.registrarReparacionCompletada(fechaEf));

        // Igual suma la reparación al semáforo y registra la fecha
        assertEquals(1, sem.getCantidadTotalReparaciones());
        assertSame(fechaEf, orden.getFechaEfectivaReparacion());
    }
}
