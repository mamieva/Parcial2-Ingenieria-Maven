package com.ulp;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Equipo de Control")
public class EquipoDeControlTest {

    private EquipoDeControl equipo;
    private List<Miembro> miembros;

    @BeforeEach
    public void setUp() {
        miembros = List.of(
                new Miembro("M1", "Ana", "Técnica"),
                new Miembro("M2", "Beto", "Electricista"),
                new Miembro("M3", "Caro", "Ayudante"),
                new Miembro("M4", "Diego", "Chofer"));
        // El responsable es el del índice 1 (Beto)
        equipo = new EquipoDeControl("EQ-01", "Electricidad", miembros, 1);
    }

    @Test
    @DisplayName("Nace libre y con el responsable indicado por índice")
    public void testCreacion() {
        assertEquals("LIBRE", equipo.getEstado());
        assertEquals(4, equipo.getMiembros().size());
        assertEquals("Beto", equipo.getResponsable().getNombre());
    }

    @Test
    @DisplayName("ocuparEquipo lo pone OCUPADO y a sus miembros no libres")
    public void testOcupar() {
        equipo.ocuparEquipo();
        assertEquals("OCUPADO", equipo.getEstado());
        assertTrue(equipo.getMiembros().stream().noneMatch(Miembro::isEstaLibre));
    }

    @Test
    @DisplayName("liberarEquipo lo vuelve a dejar LIBRE y a sus miembros libres")
    public void testLiberar() {
        equipo.ocuparEquipo();
        equipo.liberarEquipo();
        assertEquals("LIBRE", equipo.getEstado());
        assertTrue(equipo.getMiembros().stream().allMatch(Miembro::isEstaLibre));
    }
}
