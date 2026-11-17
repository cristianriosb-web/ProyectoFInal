import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsientoTest {

    @Test
    void codigoSeFormaConFilaYNumero() {
        Asiento a = new Asiento('b', 3);
        assertEquals("B3", a.getCodigo());
        assertEquals(EstadoAsiento.DISPONIBLE, a.getEstado());
    }

    @Test
    void ocuparCambiaEstado() throws AsientoOcupadoException {
        Asiento a = new Asiento('A', 1);
        a.ocupar();
        assertEquals(EstadoAsiento.OCUPADO, a.getEstado());
        assertFalse(a.estaDisponible());
    }

    @Test
    void ocuparAsientoOcupadoLanzaExcepcion() throws AsientoOcupadoException {
        Asiento a = new Asiento('A', 1);
        a.ocupar();
        assertThrows(AsientoOcupadoException.class, a::ocupar);
    }

    @Test
    void liberarDejaDisponible() throws AsientoOcupadoException {
        Asiento a = new Asiento('A', 1);
        a.ocupar();
        a.liberar();
        assertTrue(a.estaDisponible());
    }
}
