import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    private Venta venta;
    private Asiento asiento;

    @BeforeEach
    void setUp() throws CinemaException {
        Pelicula p = new Pelicula("P1", "Interestelar", "Ciencia ficción", 169, ClasificacionPelicula.MAYORES_12);
        Sala s = new Sala("S1", "Sala 1", 2, 3);
        Funcion f = new Funcion("F1", p, s, LocalDate.now(), LocalTime.of(18, 0), 15000);
        asiento = f.buscarAsiento("A1");
        asiento.ocupar();
        Cliente c = new Cliente("1", "Ana", TipoCliente.GENERAL);
        venta = new Venta("V0001", c, f, asiento, TipoCliente.GENERAL, LocalDateTime.now(), 15000);
    }

    @Test
    void iniciaActiva() {
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertTrue(venta.generarTiquete().contains("A1"));
    }

    @Test
    void cancelarLiberaAsiento() throws VentaYaCanceladaException {
        venta.cancelar();
        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertTrue(asiento.estaDisponible());
    }

    @Test
    void cancelarDosVecesLanzaExcepcion() throws VentaYaCanceladaException {
        venta.cancelar();
        assertThrows(VentaYaCanceladaException.class, venta::cancelar);
    }
}
