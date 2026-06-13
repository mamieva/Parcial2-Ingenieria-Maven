package com.ulp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Gestión de Semáforos")
public class GestionSemaforosTest {

    // 1. Variables de instancia globales del entorno de pruebas
    private GestionSemaforosService gestion;
    private Semaforo sem;              // semáforo por defecto
    private Denunciante denunciante;
    private EquipoDeControl equipo;

    // Acumula el mensaje (@DisplayName) de cada prueba que se va ejecutando,
    // para listarlos todos juntos al finalizar la clase.
    private static final List<String> mensajesTesteados = new ArrayList<>();

    /** Instancia el servicio y los objetos base antes de cada prueba. */
    @BeforeEach
    public void setup() {
        Denuncia.reiniciarRegistro(); // aísla el registro global entre pruebas

        gestion = new GestionSemaforosService();
        sem = new Semaforo(101, "OK", "Av. 1 y Calle 2", "220V");
        gestion.registrarSemaforo(sem);

        denunciante = new Denunciante("Juan", "juan@mail.com");

        equipo = new EquipoDeControl("EQ-01", "Electricidad", List.of(
                new Miembro("M1", "Ana", "Técnica"),
                new Miembro("M2", "Beto", "Electricista"),
                new Miembro("M3", "Caro", "Ayudante"),
                new Miembro("M4", "Diego", "Chofer")), 0);
    }

    /** Confirma en consola la finalización de cada prueba individual. */
    @AfterEach
    public void teardown(TestInfo info) {
        System.out.println("✓ Prueba finalizada: " + info.getDisplayName());
        mensajesTesteados.add(info.getDisplayName());
    }

    /** Al terminar todas las pruebas, lista todos los mensajes testeados. */
    @AfterAll
    public static void resumen() {
        System.out.println("\n===== Resumen: mensajes testeados (" + mensajesTesteados.size() + ") =====");
        for (int i = 0; i < mensajesTesteados.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + mensajesTesteados.get(i));
        }
        System.out.println("=================================================\n");
    }

    // 2. Prueba de Composición Estricta (Semaforo y Luz)
    @Test
    @DisplayName("El semáforo se compone de exactamente 3 luces sin duplicar instancias")
    public void testComposicionEstricta() {
        // Se crean exactamente 3 luces
        assertEquals(3, sem.getLuces().size());

        // La luz en la posición 0 es siempre la misma instancia (no se duplica)
        Luz primera = sem.getLuces().get(0);
        assertSame(primera, sem.getLuces().get(0));
    }

    // 3. Prueba de Robustez ante Duplicados (assertThrows y @Timeout)
    @Test
    @Timeout(value = 400, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Asignar una segunda orden a la misma denuncia lanza OrdenYaAsignadaException")
    public void testNoSeAsignaSegundaOrden() {
        // Denuncia que ya posee una orden de composición asignada
        Denuncia denuncia = new Denuncia("D-1", new Date(), "Av. 1", "Calle 2",
                "Luz roja quemada", "Alta", denunciante, sem);
        OrdenDeComposicion primera = new OrdenDeComposicion(5001, new Date(), "Primera orden", denuncia);
        gestion.asignarOrden(denuncia, primera);

        // Intentar asociarle una segunda orden debe disparar la excepción
        OrdenDeComposicion segunda = new OrdenDeComposicion(5002, new Date(), "Segunda orden", denuncia);
        assertThrows(OrdenYaAsignadaException.class,
                () -> gestion.asignarOrden(denuncia, segunda));
    }

    // 4. Prueba de Flujo de Reparación Exitoso (assertTrue y assertEquals)
    @Test
    @DisplayName("Al completar la reparación el equipo queda Libre y sus 4 miembros libres")
    public void testFlujoReparacionExitoso() {
        Denuncia denuncia = new Denuncia("D-1", new Date(), "Av. 1", "Calle 2",
                "Luz roja quemada", "Alta", denunciante, sem);
        OrdenDeComposicion orden = new OrdenDeComposicion(5001, new Date(), "Reemplazo de luz", denuncia);
        orden.asignarEquipo(equipo); // el equipo queda OCUPADO

        // El responsable carga la fecha efectiva: la orden pasa a completada
        orden.registrarReparacionCompletada(new Date());

        // El equipo de control vuelve a estar "Libre"
        assertEquals(EquipoDeControl.LIBRE, equipo.getEstado());

        // Los 4 miembros del equipo quedan en estado libre = true
        assertEquals(4, equipo.getMiembros().size());
        for (Miembro m : equipo.getMiembros()) {
            assertTrue(m.isEstaLibre(), "El miembro " + m.getNombre() + " debe quedar libre");
        }
    }

    // 5. Prueba Parametrizada de Prioridades (@ParameterizedTest)
    @ParameterizedTest
    @ValueSource(strings = {"Alta", "Media", "Baja"})
    @DisplayName("esPrioridadValida retorna true para los tres niveles estrictos")
    public void testPrioridadesValidas(String prioridad) {
        Denuncia denuncia = new Denuncia("D-1", new Date(), "X", "Y", "falla", prioridad, denunciante, sem);
        assertTrue(denuncia.esPrioridadValida(),
                "La prioridad '" + prioridad + "' debería ser válida");
    }

    // 6. Prueba de Métricas Estadísticas e Historial (assertEquals)
    @Test
    @DisplayName("El servicio cuenta el histórico de denuncias de un semáforo (debe ser 3)")
    public void testContadorHistoricoDeDenuncias() {
        // 3 denuncias diferentes asociadas al mismo semáforo (nro 101)
        new Denuncia("D-1", new Date(), "Av. 1", "Calle 2", "Luz roja quemada", "Alta", denunciante, sem);
        new Denuncia("D-2", new Date(), "Av. 1", "Calle 2", "Luz amarilla intermitente", "Media", denunciante, sem);
        new Denuncia("D-3", new Date(), "Av. 1", "Calle 2", "Poste flojo", "Baja", denunciante, sem);

        assertEquals(3, gestion.contarDenunciasHistoricas(101));
    }
}
