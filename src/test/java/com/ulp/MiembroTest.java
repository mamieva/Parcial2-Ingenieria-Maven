package com.ulp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Miembro")
public class MiembroTest {

    @Test
    @DisplayName("Un miembro nace estando libre")
    public void testNaceLibre() {
        Miembro m = new Miembro("M1", "Ana", "Técnica");
        assertTrue(m.isEstaLibre());
    }

    @Test
    @DisplayName("setEstaLibre cambia la disponibilidad")
    public void testSetEstaLibre() {
        Miembro m = new Miembro("M1", "Ana", "Técnica");
        m.setEstaLibre(false);
        assertFalse(m.isEstaLibre());
        m.setEstaLibre(true);
        assertTrue(m.isEstaLibre());
    }
}
