import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SalaTest {
    private Sala sala;

    @BeforeEach
    void setUp() {
        sala = new Sala("S1", "Sala principal", 3, 4);
    }

    @Test
    void testGenerarAsientosYCapacidad() {
        assertEquals(12, sala.getCapacidad());
        assertEquals(12, sala.getAsientos().size());
        assertEquals("A1", sala.getAsientos().get(0).getCodigo());
        assertEquals("C4", sala.getAsientos().get(11).getCodigo());
    }

    @Test
    void testCrearCopiaAsientosIndependientes() {
        List<Asiento> copia = sala.crearCopiaAsientos();
        copia.get(0).ocupar();
        assertTrue(sala.getAsientos().get(0).estaDisponible());
        assertFalse(copia.get(0).estaDisponible());
    }
    @Test
    void testHabilitarYDeshabilitarSala() {
        assertTrue(sala.estaDisponible());
        sala.deshabilitar();
        assertFalse(sala.estaDisponible());
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala.getEstado());
    }
}