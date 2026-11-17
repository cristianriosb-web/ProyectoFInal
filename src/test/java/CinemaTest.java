import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

class CinemaTest {
    private Cinema cinema;
    private Pelicula pelicula;
    private Sala sala;
    private Funcion funcion;
    private Cliente clienteEstudiante;

    @BeforeEach
    void setUp() throws Exception {
        cinema = new Cinema("CINEMAUQ");
        Pelicula P1= new Pelicula("P1", "El Último Algoritmo", "Drama", 120, ClasificacionPelicula.TODOS);
    cinema.registrarPelicula(P1);
        Sala s1 = new Sala("S1", "Sala 1", 2, 2);
        cinema.registrarSala(s1);
        funcion = cinema.programarFuncion("F1", "P1", "S1", LocalDate.of(2026, 11, 20), LocalTime.of(18, 30), 15000);
        clienteEstudiante = new Cliente("1094000111", "Jacobo", TipoCliente.ESTUDIANTE);
    }

    // --- PRUEBAS DE VENTAS Y CANCELACIONES ---

    @Test
    void testVenderEntradaConDescuentoEstudiante() throws Exception {
        // Descuento estudiante por defecto es 20% -> 15000 * 0.80 = 12000
        Venta venta = cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.ESTUDIANTE, "A1");

        assertNotNull(venta);
        assertEquals(12000.0, venta.getValorPagado(), 0.01);
        assertFalse(funcion.buscarAsiento("A1").estaDisponible());
        assertEquals(3, funcion.contarDisponibles());
    }

    @Test
    void testCancelarVentaLiberaAsientoYRestaIngresos() throws Exception {
        Venta venta = cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.ESTUDIANTE, "A1");
        assertEquals(1, cinema.getTotalEntradasVendidas());

        cinema.cancelarVenta(venta.getCodigo());

        assertFalse(venta.estaActiva());
        assertTrue(funcion.buscarAsiento("A1").estaDisponible()); // Asiento liberado
        assertEquals(0, cinema.getTotalEntradasVendidas());
        assertEquals(0.0, cinema.getTotalIngresos(), 0.01);
    }

    // --- PRUEBAS DE REPORTES ---

    @Test
    void testReportesTotalesYPeliculaMasVendida() throws Exception {
        Pelicula p2 = new Pelicula("P2", "Matrix", "Acción", 136, ClasificacionPelicula.MAYORES_15);

        cinema.registrarPelicula(p2);
        Funcion f2 = cinema.programarFuncion("F2", "P2", "S1", LocalDate.of(2026, 11, 21), LocalTime.of(20, 0), 15000);

        // 2 ventas para P1 (12.000 c/u) y 1 venta General para P2 (15.000)
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.ESTUDIANTE, "A1");
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.ESTUDIANTE, "A2");
        cinema.venderEntrada("F2", clienteEstudiante, TipoCliente.GENERAL, "A1");

        assertEquals(3, cinema.getTotalEntradasVendidas());
        assertEquals(39000.0, cinema.getTotalIngresos(), 0.01);
        assertEquals(pelicula, cinema.getPeliculaMasVendida());
        assertEquals(1, cinema.getFuncionesProgramadasPorFecha(LocalDate.of(2026, 11, 20)).size());
    }

    // --- PRUEBAS DE EXCEPCIONES (Validaciones del documento) ---

    @Test
    void testExcepcionCodigoDuplicadoPeliculaYSala() {
        assertThrows(CodigoDuplicadoException.class, () -> {
            Pelicula p1= new Pelicula("p1","Titanic","Drama",189, ClasificacionPelicula.MAYORES_7);
            cinema.registrarPelicula(p1);
        });

        assertThrows(CodigoDuplicadoException.class, () -> {
            Sala s1 = new Sala("S1", "Sala 1", 2, 2);
            cinema.registrarSala(s1);
        });
    }

    @Test
    void testExcepcionProgramarEnSalaFueraDeServicio() throws Exception {
        cinema.deshabilitarSala("S1");

        assertThrows(SalaNoDisponibleException.class, () -> {
            cinema.programarFuncion("F2", "P1", "S1", LocalDate.of(2026, 11, 22), LocalTime.of(16, 0), 15000);
        });
    }

    @Test
    void testExcepcionVenderEnFuncionInexistente() {
        assertThrows(FuncionNoEncontradaException.class, () -> {
            cinema.venderEntrada("F999", clienteEstudiante, TipoCliente.GENERAL, "A1");
        });
    }

    @Test
    void testExcepcionVenderAsientoOcupado() throws Exception {
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "A1");

        assertThrows(AsientoOcupadoException.class, () -> {
            cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "A1");
        });
    }

    @Test
    void testExcepcionSinAsientosDisponibles() throws Exception {
        // La sala tiene capacidad 4 (A1, A2, B1, B2)
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "A1");
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "A2");
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "B1");
        cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "B2");

        // Intentar vender una quinta entrada debe lanzar SinAsientosDisponiblesException
        assertThrows(SinAsientosDisponiblesException.class, () -> {
            cinema.venderEntrada("F1", clienteEstudiante, TipoCliente.GENERAL, "A1");
        });
    }

    @Test
    void testExcepcionCancelarVentaInexistente() {
        assertThrows(VentaNoEncontradaException.class, () -> {
            cinema.cancelarVenta("V9999");
        });
    }
}