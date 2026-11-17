import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FuncionTest {

    private Funcion funcion;

    @BeforeEach
    void setUp() throws CinemaException {
        Pelicula p = new Pelicula("P1", "Interestelar", "Ciencia ficción", 169, ClasificacionPelicula.MAYORES_12);
        Sala s = new Sala("S1", "Sala 1", 2, 3);
        funcion = new Funcion("F1", p, s, LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), 15000);
    }

    @Test
    void iniciaProgramadaYConTodosLosAsientosLibres() {
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertEquals(6, funcion.getTotalAsientos());
        assertEquals(6, funcion.contarDisponibles());
        assertEquals(0, funcion.contarOcupados());
    }

    @Test
    void cuentaOcupadosYDisponibles() throws CinemaException {
        funcion.buscarAsiento("a1").ocupar();
        funcion.buscarAsiento("B3").ocupar();
        assertEquals(2, funcion.contarOcupados());
        assertEquals(4, funcion.getAsientosDisponibles().size());
        assertTrue(funcion.hayAsientosDisponibles());
    }

    @Test
    void asientoInexistenteLanzaExcepcion() {
        assertThrows(AsientoNoEncontradoException.class, () -> funcion.buscarAsiento("Z9"));
    }

    @Test
    void cambiosDeEstado() {
        funcion.cancelar();
        assertEquals(EstadoFuncion.CANCELADA, funcion.getEstado());
        funcion.finalizar();
        assertEquals(EstadoFuncion.FINALIZADA, funcion.getEstado());
        assertFalse(funcion.estaProgramada());
    }

    @Test
    void valorBaseInvalidoLanzaExcepcion() {
        assertThrows(CinemaException.class,
                () -> funcion.actualizar(LocalDate.now(), LocalTime.NOON, 0));
    }
}
