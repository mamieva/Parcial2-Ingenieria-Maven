package com.ulp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Semáforo")
public class SemaforoTest {

    private Semaforo sem;

    @BeforeEach
    public void setUp() {
        sem = new Semaforo(101, "OK", "Av. 1 y Calle 2", "220V");
    }

    @Test
    @DisplayName("Se crea con sus datos y 3 luces por composición")
    public void testCreacionCon3Luces() {
        assertEquals(101, sem.getNro());
        assertEquals("220V", sem.getTipoCorriente());
        assertEquals(3, sem.getLuces().size());
    }

    @Test
    @DisplayName("Las 3 luces son Rojo, Amarillo y Verde")
    public void testColoresDeLasLuces() {
        assertEquals("Rojo", sem.getLuces().get(0).getColor());
        assertEquals("Amarillo", sem.getLuces().get(1).getColor());
        assertEquals("Verde", sem.getLuces().get(2).getColor());
    }

    @Test
    @DisplayName("get/set del estado")
    public void testEstado() {
        assertEquals("OK", sem.getEstado());
        sem.setEstado("FALLANDO");
        assertEquals("FALLANDO", sem.getEstado());
    }

    @Test
    @DisplayName("Un semáforo nuevo no tiene reparaciones")
    public void testReparacionesIniciales() {
        assertEquals(0, sem.getCantidadTotalReparaciones());
    }

    @Test
    @DisplayName("agregarDenunciaHistorial suma al historial")
    public void testAgregarDenunciaHistorial() {
        Denunciante den = new Denunciante("Ana", "ana@mail.com");
        new Denuncia("D-1", new java.util.Date(), "X", "Y", "falla", "ALTA", den, sem);
        assertEquals(1, sem.getHistorialDenuncias().size());
    }
}
