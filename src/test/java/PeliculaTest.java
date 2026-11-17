import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaTest {

    @Test
    void creaPeliculaDisponible() throws DatoInvalidoException {
        Pelicula p = new Pelicula("P1", "Interestelar", "Ciencia ficción", 169, ClasificacionPelicula.MAYORES_12);
        assertEquals(EstadoPelicula.DISPONIBLE, p.getEstado());
        assertEquals(169, p.getDuracionMinutos());
    }

    @Test
    void datosInvalidosLanzanExcepcion() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("", "X", "Drama", 100, ClasificacionPelicula.TODOS));
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", "X", "Drama", 0, ClasificacionPelicula.TODOS));
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", "X", "Drama", 100, null));
    }

    @Test
    void actualizarYDeshabilitar() throws DatoInvalidoException {
        Pelicula p = new Pelicula("P1", "A", "Drama", 100, ClasificacionPelicula.TODOS);
        p.actualizar("B", "Comedia", 90, ClasificacionPelicula.MAYORES_7);
        assertEquals("B", p.getNombre());
        p.deshabilitar();
        assertEquals(EstadoPelicula.NO_DISPONIBLE, p.getEstado());
        p.habilitar();
        assertTrue(p.estaDisponible());
    }
}
