import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionTest {
    private Pelicula pelicula;
    private Sala sala;
    private Funcion funcion;

    @BeforeEach
    void setUp() {
        Pelicula pelicula1= new Pelicula("P1", "Interestelar", "Ciencia Ficción", 169, ClasificacionPelicula.MAYORES_12);
        Sala sala1= new Sala("S1", "Sala 1", 2, 3); // 6 asientos
        Funcion funcion1 = new Funcion("F1",pelicula1 , sala1, LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), 15000);
    }

    @Test
    void testConteoDisponiblesYOcupados() {
        assertEquals(6, funcion.contarDisponibles());
        assertEquals(0, funcion.contarOcupados());

        funcion.buscarAsiento("A1").ocupar();
        funcion.buscarAsiento("B2").ocupar();

        assertEquals(4, funcion.contarDisponibles());
        assertEquals(2, funcion.contarOcupados());
        assertTrue(funcion.hayAsientosDisponibles());
    }

    @Test
    void testCompareToOrdenCronologico() {

        Funcion funcionTemprana = new Funcion("F2", pelicula, sala, LocalDate.of(2026, 11, 20), LocalTime.of(14, 0), 15000);
        assertTrue(funcionTemprana.compareTo(funcion1) < 0); // 14:00 va antes que 18:00
    }
}
