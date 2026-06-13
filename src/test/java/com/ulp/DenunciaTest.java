package com.ulp;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Denuncia")
public class DenunciaTest {

    private Semaforo sem;
    private Denunciante juan;

    @BeforeEach
    public void setUp() {
        // Aísla el registro global entre pruebas
        Denuncia.reiniciarRegistro();
        sem = new Semaforo(101, "FALLANDO", "Av. 1", "220V");
        juan = new Denunciante("Juan", "juan@mail.com");
    }

    @Test
    @DisplayName("Al crearse enlaza semáforo y denunciante (bidireccional)")
    public void testEnlaceBidireccional() {
        Denuncia d = new Denuncia("D-1", new Date(), "X", "Y", "falla", "ALTA", juan, sem);

        assertSame(sem, d.getSemaforoAsociado());
        assertSame(juan, d.getDenunciante());
        // Se registró de los dos lados
        assertEquals(1, sem.getHistorialDenuncias().size());
        assertEquals(1, juan.getMisDenuncias().size());
    }

    @Test
    @DisplayName("listDenunciaPorSemaforo filtra por número de semáforo")
    public void testListPorSemaforo() {
        Semaforo otro = new Semaforo(202, "OK", "Av. 2", "220V");
        Denuncia d1 = new Denuncia("D-1", new Date(), "X", "Y", "falla", "ALTA", juan, sem);
        new Denuncia("D-2", new Date(), "X", "Y", "falla", "BAJA", juan, otro);

        assertEquals(1, d1.listDenunciaPorSemaforo(101).size());
        assertEquals(1, d1.listDenunciaPorSemaforo(202).size());
        assertEquals(0, d1.listDenunciaPorSemaforo(999).size());
    }

    @Test
    @DisplayName("listDenunciaPorDenunciante filtra por mail")
    public void testListPorDenunciante() {
        Denunciante ana = new Denunciante("Ana", "ana@mail.com");
        Denuncia d1 = new Denuncia("D-1", new Date(), "X", "Y", "falla", "ALTA", juan, sem);
        new Denuncia("D-2", new Date(), "X", "Y", "falla", "BAJA", juan, sem);
        new Denuncia("D-3", new Date(), "X", "Y", "falla", "BAJA", ana, sem);

        assertEquals(2, d1.listDenunciaPorDenunciante("juan@mail.com").size());
        assertEquals(1, d1.listDenunciaPorDenunciante("ana@mail.com").size());
    }
}
